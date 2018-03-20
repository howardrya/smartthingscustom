/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Custom Outdoor Lighting", namespace: "smartthings/testing", author: "SmartThings") {
		capability "Actuator"
		capability "Button"
		capability "Configuration"
		capability "Sensor"

        command "push1"
        command "push2"
        command "push3"
        command "push4"
        command "push5"
        command "push6"
        command "push7"
        command "push8"
        command "push9"
        command "push10"
        command "push11"
        command "push12"
        command "push13"
        command "push14"
        command "push15"
        command "push16"
        command "push17"
        command "push18"

	}

	simulator {
		status "button 1 pushed":  "command: 2001, payload: 01"
		status "button 2 pushed":  "command: 2001, payload: 29"
		status "button 3 pushed":  "command: 2001, payload: 51"
		status "button 4 pushed":  "command: 2001, payload: 79"
        status "button 5 pushed":  "command: 2001, payload: 02"
		status "button 6 pushed":  "command: 2001, payload: 30"
		status "button 7 pushed":  "command: 2001, payload: 52"
		status "button 8 pushed":  "command: 2001, payload: 80"
        status "button 9 pushed":  "command: 2001, payload: 03"
		status "button 10 pushed":  "command: 2001, payload: 31"
		status "button 11 pushed":  "command: 2001, payload: 53"
		status "button 12 pushed":  "command: 2001, payload: 81"
        status "button 13 pushed":  "command: 2001, payload: 04"
		status "button 14 pushed":  "command: 2001, payload: 32"
		status "button 15 pushed":  "command: 2001, payload: 54"
		status "button 16 pushed":  "command: 2001, payload: 82"
        status "button 17 pushed":  "command: 2001, payload: 55"
		status "button 18 pushed":  "command: 2001, payload: 83"
		status "wakeup":  "command: 8407, payload: "
	}
	tiles {
		standardTile("button", "device.button") {
			state "default", label: "", icon: "st.unknown.zwave.remote-controller", backgroundColor: "#ffffff"
		}
 		standardTile("push1", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Default White", backgroundColor: "#ffffff", action: "push1"
		}
 		standardTile("push2", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Teal Lights", backgroundColor: "#ffffff", action: "push2"
		}
 		standardTile("push3", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Police Lights", backgroundColor: "#ffffff", action: "push3"
		}
 		standardTile("push4", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Rainbow Fade", backgroundColor: "#ffffff", action: "push4"
		}
 		standardTile("push5", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Flag Lights", backgroundColor: "#ffffff", action: "push5"
		}
 		standardTile("push6", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Christmas Lights", backgroundColor: "#ffffff", action: "push6"
		}
 		standardTile("push7", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Rainbow Solid", backgroundColor: "#ffffff", action: "push7"
		}
 		standardTile("push8", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Teal and Green", backgroundColor: "#ffffff", action: "push8"
		}
        standardTile("push9", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Green", backgroundColor: "#ffffff", action: "push9"
		}
 		standardTile("push10", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Green and White", backgroundColor: "#ffffff", action: "push10"
		}
 		standardTile("push11", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Push 11", backgroundColor: "#ffffff", action: "push11"
		}
 		standardTile("push12", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Push 12", backgroundColor: "#ffffff", action: "push12"
		}
 		standardTile("push13", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Push 13", backgroundColor: "#ffffff", action: "push13"
		} 
 		standardTile("push14", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Push 14", backgroundColor: "#ffffff", action: "push14"
		}
 		standardTile("push15", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Push 15", backgroundColor: "#ffffff", action: "push15"
		}
 		standardTile("push16", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Push 16", backgroundColor: "#ffffff", action: "push16"
		}
        standardTile("push17", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Push 17", backgroundColor: "#ffffff", action: "push17"
		}
 		standardTile("push18", "device.button", width: 1, height: 1, decoration: "flat") {
			state "default", label: "Push 18", backgroundColor: "#ffffff", action: "push18"
		}

		main "button"
		details(["push1","push2","push3","push4","push5","push6","push7","push8","push9","push10","push11","push12","push13","push14","push15","push16","push17","push18"])
	}
}

def parse(String description) {

}

def push1() {
	push(1)
}

def push2() {
	push(2)
}

def push3() {
	push(3)
}

def push4() {
	push(4)
}

def push5() {
	push(5)
}

def push6() {
	push(6)
}

def push7() {
	push(7)
}

def push8() {
	push(8)
}

def push9() {
	push(9)
}

def push10() {
	push(10)
}

def push11() {
	push(11)
}

def push12() {
	push(12)
}

def push13() {
	push(13)
}

def push14() {
	push(14)
}

def push15() {
	push(15)
}

def push16() {
	push(16)
}

def push17() {
	push(17)
}

def push18() {
	push(18)
}

private push(button) {
	log.debug "$device.displayName button $button was pushed"
	sendEvent(name: "button", value: "pushed", data: [buttonNumber: button], descriptionText: "$device.displayName button $button was pushed", isStateChange: true)
}
