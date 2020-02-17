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

  readProject() {
    this.connection.send("READ_PROJECT");
  }

  // Send a message to the backend to run a certain test
	runSelected(guid: string, project): void {
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
    console.log(event.data);
		let message = JSON.parse(event.data);
    this.receivedMessageSubject.next(message);
  }

  onOpen(event: any) {
    this.connectionStateSubject.next(WebSocketState.OPEN);
  }

  sendMessage(message) {
    this.connection.send(message);
  }

  startMock(project: string) {
    this.sendMessage('START_MOCK ' + project);
  }

  stopMock() {
    this.sendMessage('STOP_MOCK');
  }
}
