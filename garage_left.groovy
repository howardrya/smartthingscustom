import groovy.json.JsonSlurper

metadata {
	definition (name: "NodeMCU Garage Door Control - Left", namespace: "smartthings", author: "Ryan") {
		capability "Actuator"
		capability "Door Control"
		capability "Garage Door Control"
		capability "Contact Sensor"
		capability "Refresh"
		capability "Sensor"
        capability "Momentary"
        capability "Switch"
        capability "Button"
        command "openleft"
        command "refresh"
        command "RebootNow"
        command "ResetTiles"
	}
	preferences {
		input("DeviceIP", "string", title:"Device IP Address", description: "Please enter your device's IP Address", required: true, displayDuringSetup: true)
		input("DevicePort", "string", title:"Device Port", description: "Empty assumes port 80.", required: false, displayDuringSetup: true)
		input("DevicePath", "string", title:"URL Path", description: "Rest of the URL, include forward slash.", displayDuringSetup: true)
		input(name: "DevicePostGet", type: "enum", title: "POST or GET. POST for PHP & GET for Arduino.", options: ["POST","GET"], defaultValue: "POST", required: false, displayDuringSetup: true)
		input("UseJSON", "bool", title:"Use JSON instead of HTML?", description: "", defaultValue: false, required: false, displayDuringSetup: true)
		section() {
			input("HTTPAuth", "bool", title:"Requires User Auth?", description: "Choose if the HTTP requires basic authentication", defaultValue: false, required: true, displayDuringSetup: true)
			input("HTTPUser", "string", title:"HTTP User", description: "Enter your basic username", required: false, displayDuringSetup: true)
			input("HTTPPassword", "string", title:"HTTP Password", description: "Enter your basic password", required: false, displayDuringSetup: true)
		}
	}
	simulator {
	}

tiles(scale: 2) {
         multiAttributeTile(name:"status", type: "generic", width: 6, height: 4){
        tileAttribute ("device.contact", key: "PRIMARY_CONTROL") {
            attributeState "open", label:'${name}', icon:"st.doors.garage.garage-open", backgroundColor:"#ffa81e"
            attributeState "closed", label:'${name}', icon:"st.doors.garage.garage-closed", backgroundColor:"#79b821"
        }
    }
        standardTile("openleft", "device.Button", width: 2, height: 2, decoration: "flat") {
			state "iconLabel", label: 'Left Door', action: "openleft", icon:"st.doors.garage.garage-closed", backgroundColor: "#ffffff", nextState: "pushed"
            state "pushed", label: 'pushed', action: "ResetTiles", icon: "st.secondary.refresh-icon", backgroundColor: "#44b621", nextState: "iconLabel"
		}

		standardTile("RefreshTrigger", "device.refreshswitch", width: 1, height: 1, decoration: "flat") {
			state "default", label:'REFRESH', action: "refresh.refresh", icon: "st.secondary.refresh-icon", backgroundColor:"#53a7c0", nextState: "refreshing"
			state "refreshing", label: 'REFRESHING', action: "ResetTiles", icon: "st.secondary.refresh-icon", backgroundColor: "#FF6600", nextState: "default"
		}

		standardTile("RebootNow", "device.rebootnow", width: 1, height: 1, decoration: "flat") {
			state "default", label:'REBOOT' , action: "RebootNow", icon: "st.Seasonal Winter.seasonal-winter-014", backgroundColor:"#ff0000", nextState: "rebooting"
			state "rebooting", label: 'REBOOTING', action: "ResetTiles", icon: "st.Office.office13", backgroundColor: "#FF6600", nextState: "default"
		}
		main "openleft"
		details(["status","openleft", "RefreshTrigger", "RebootNow"])
	}
}


def refresh() {
	runCmd('Refresh=')
     log.debug "refresh"
}
def RebootNow() {
	log.debug "Reboot Triggered!!!"
	runCmd('RebootNow=')
}
def poll() {
	refresh()
}
def ping() {
    log.debug "ping()"
	refresh()
}
def openleft() {
	log.debug "openleft"
	runCmd('RELAY1=MOMENTARY')
}

def runCmd(String varCommand) {
	def host = DeviceIP
	def hosthex = convertIPtoHex(host).toUpperCase()
	def LocalDevicePort = ''
	if (DevicePort==null) { LocalDevicePort = "80" } else { LocalDevicePort = DevicePort }
	def porthex = convertPortToHex(LocalDevicePort).toUpperCase()
	device.deviceNetworkId = "$hosthex:$porthex"
	def userpassascii = "${HTTPUser}:${HTTPPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()

	log.debug "The device id configured is: $device.deviceNetworkId"

	def headers = [:]
	headers.put("HOST", "$host:$LocalDevicePort")
	headers.put("Content-Type", "application/x-www-form-urlencoded")
	if (HTTPAuth) {
		headers.put("Authorization", userpass)
	}
	log.debug "The Header is $headers"

	def path = ''
	def body = ''
	log.debug "Uses which method: $DevicePostGet"
	def method = "POST"
	try {
		if (DevicePostGet.toUpperCase() == "GET") {
			method = "GET"
			path = varCommand
			if (path.substring(0,1) != "/") { path = "/" + path }
			log.debug "GET path is: $path"
		} else {
			path = DevicePath
			body = varCommand
			log.debug "POST body is: $body"
		}
		log.debug "The method is $method"
	}
	catch (Exception e) {
		settings.DevicePostGet = "POST"
		log.debug e
		log.debug "You must not have set the preference for the DevicePOSTGET option"
	}

	try {
		def hubAction = new physicalgraph.device.HubAction(
			method: method,
			path: path,
			body: body,
			headers: headers
			)
		hubAction.options = [outputMsgToS3:false]
		log.debug hubAction
		hubAction
	}
	catch (Exception e) {
		log.debug "Hit Exception $e on $hubAction"
	}
}

def parse(String description) {
//	log.debug "Parsing '${description}'"
	def whichTile = ''
	def map = [:]
	def retResult = []
	def descMap = parseDescriptionAsMap(description)
	def jsonlist = [:]
	def bodyReturned = ' '
	def headersReturned = ' '
	if (descMap["body"]) { bodyReturned = new String(descMap["body"].decodeBase64()) }
	if (descMap["headers"]) { headersReturned = new String(descMap["headers"].decodeBase64()) }
	//log.debug "BODY---" + bodyReturned
	//log.debug "HEADERS---" + headersReturned

	if (descMap["body"]) {
		if (headersReturned.contains("application/json")) {
			def body = new String(descMap["body"].decodeBase64())
			def slurper = new JsonSlurper()
			jsonlist = slurper.parseText(body)
			//log.debug "JSONLIST---" + jsonlist."CPU"
			jsonlist.put ("Date", new Date().format("yyyy-MM-dd h:mm:ss a", location.timeZone))
		} else {
			jsonlist.put ("Date", new Date().format("yyyy-MM-dd h:mm:ss a", location.timeZone))
			def data=bodyReturned.eachLine { line ->
			if (line.contains('Contact Sensor=Open')) { jsonlist.put ("SensorPinStatus".replace("=",""), "Open") }
			if (line.contains('Contact Sensor=Closed')) { jsonlist.put ("SensorPinStatus".replace("=",""), "Closed") }
		}
	}
}
if (device.currentState("contact")==null) {sendEvent(name: "contact", value: "closed", descriptionText: "$device.displayName is closed")}
		if (jsonlist."SensorPinStatus"=="Open") {
			if (device.currentState("contact").getValue()=="closed") { sendEvent(name: "sensorTriggered", value: "OPEN @ " + jsonlist."Date", unit: "") }
			sendEvent(name: "contact", value: "open", descriptionText: "$device.displayName is open")
			sendEvent(name: "refreshswitch", value: "default", isStateChange: true)
		} else if (jsonlist."SensorPinStatus"=="Closed") {
			if (device.currentState("contact").getValue()=="open") { sendEvent(name: "sensorTriggered", value: "CLOSED @ " + jsonlist."Date", unit: "") }
			sendEvent(name: "contact", value: "closed", descriptionText: "$device.displayName is closed")
			sendEvent(name: "refreshswitch", value: "default", isStateChange: true)
		} else {
			sendEvent(name: "contact", value: "closed", descriptionText: "$device.displayName is closed")
			sendEvent(name: "refreshswitch", value: "default", isStateChange: true)
		}
}
def parseDescriptionAsMap(description) {
	description.split(",").inject([:]) { map, param ->
	def nameAndValue = param.split(":")
	map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
	}
}
private String convertIPtoHex(ipAddress) {
	String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
	//log.debug "IP address entered is $ipAddress and the converted hex code is $hex"
	return hex
}
private String convertPortToHex(port) {
	String hexport = port.toString().format( '%04x', port.toInteger() )
	//log.debug hexport
	return hexport
}
private Integer convertHexToInt(hex) {
	Integer.parseInt(hex,16)
}
private String convertHexToIP(hex) {
	//log.debug("Convert hex to ip: $hex")
	[convertHexToInt(hex[0..1]),convertHexToInt(hex[2..3]),convertHexToInt(hex[4..5]),convertHexToInt(hex[6..7])].join(".")
}
private getHostAddress() {
	def parts = device.deviceNetworkId.split(":")
	//log.debug device.deviceNetworkId
	def ip = convertHexToIP(parts[0])
	def port = convertHexToInt(parts[1])
	return ip + ":" + port
}
