import { Component } from '@angular/core';

import { CrudService } from './crud.service';
import { WebSocketState } from './websocket-state.enum';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 's-mock';

  constructor(private _crudService: CrudService) {

    this._crudService.connectionState.subscribe((state: WebSocketState) => {
      if (state === WebSocketState.OPEN) {
        console.log('WebSocket connection opened!');
      }
    });

    this._crudService.receivedMessage.subscribe((message: any) => {
      if (message.name == "_initial") {
					// ignore
			} else if (message.name == "_executionStart") {
				// ignore
			} else if (message.name == "_executionCompleted") {
				// ignore
			} else if (message.name == "_extractionCompleted") {
				// ignore
			} else if (message.name == '_projectSaved') {
				// ignore
			} else { // Read project
				// this.project = message;
        console.log(message);
			}
    });
  }
}
