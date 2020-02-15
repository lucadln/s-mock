import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

import { WebSocketState } from './websocket-state.enum';

@Injectable({
  providedIn: 'root'
})
export class CrudService {
  connection: WebSocket;
  connectionStateSubject: BehaviorSubject<WebSocketState>
					= new BehaviorSubject<WebSocketState>(WebSocketState.CLOSED);
  connectionState: Observable<WebSocketState>
					= this.connectionStateSubject.asObservable();

  receivedMessageSubject: BehaviorSubject<any>
					= new BehaviorSubject<any>(new Object(JSON.parse('{"name": "_initial"}')));
	receivedMessage: Observable<string>
					= this.receivedMessageSubject.asObservable();

  constructor() {
    this.createWebSocketConnection();
    this.initWebsocketConnection();
	}

  createWebSocketConnection() {
    this.connection = new WebSocket("ws://localhost:8888/s-mock");
  }

  initWebsocketConnection () {
    this.connection.onopen = (event) => this.onOpen(event);
    this.connection.onmessage = (event) => this.onMessage(event);
    this.connection.onclose = (event) => this.onClose(event);
  }

  // Send a message to the backend to run a certain test
	runSelected(guid: string, project): void {
    console.log('Got back in the CRUD service. The error is not yet here?');
    console.log(this.connection);
		this.connection.send(
						"RUN SELECTED "
						+ guid
						+ ", "
						+ JSON.stringify(project));
	}

  onClose(event: any) {
    this.connectionStateSubject.next(WebSocketState.CLOSED);
    console.log('Backend connection closed!');

    // Try reconnecting every 3 seconds
    let reconnect = setInterval(function() {
      if (this.connection.readyState != 0) {
        if (this.connection.readyState != 1) {
          console.log('Trying to reconnect to the backend server...');
          try {
            this.createWebSocketConnection();
          } catch (ex) { console.warn(ex); }
        } else { // If the connection is already open...
          console.log('Reconnected to the backend server.');
          this.initWebsocketConnection();

          clearInterval(reconnect);
        }
      }
    }.bind(this), 3000);
  }

  // Listen for messages from the backend server
	onMessage(event: any) {
		let message = JSON.parse(event.data);

		if (message.name == "_initial") {
      // @TODO: is this still neeeded?
		} else if (message.name == "_executionStart") {
			this.receivedMessageSubject.next(message);
		} else if (message.name == "_executionCompleted") {
			this.receivedMessageSubject.next(message);
		} else if (message.name == "_extractionCompleted") {
			this.receivedMessageSubject.next(message);
		} else { // Received a project object from the backend
      // @TODO: is this still needed?
      // this.notifyProjectOpened(new CrudHandler().getProjectFromString(event.data));
		}
  }

  onOpen(event: any) {
    this.connectionStateSubject.next(WebSocketState.OPEN);
  }

  sendMessage(message) {
    this.receivedMessageSubject.next(message);
  }
}
