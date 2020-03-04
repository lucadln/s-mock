import { HostListener, Component } from '@angular/core';

import { Action } from './../definition/action';
import { ActionType } from './../definition/action-type.enum';
import { CrudService } from './crud.service';
import { HTMLElementHandler } from './../utils/html-element-handler'
import { Project } from './../definition/project';
import { WebSocketState } from './websocket-state.enum';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  ActionType = ActionType;

  mockRunning: boolean = false;
  openedPopUpsIds = [];
  project: any = null;
  title: string = 's-mock';

  constructor(private _crudService: CrudService) {

    this._crudService.connectionState.subscribe((state: WebSocketState) => {
      if (state === WebSocketState.OPEN) {
        this._crudService.readProject();
        console.log('WebSocket connection opened!');
      }
    });

    this._crudService.receivedMessage.subscribe((message: any) => {
      if (message.type == 'READ_PROJECT_RESPONSE') {
        this.project = message.content;
        console.log('Read the project from the backend');
        console.log(this.project);
      } else if (message.type == 'START_MOCK_RESPONSE') {
        if (message.content.success == true) {
          this.mockRunning = true;
          HTMLElementHandler.removeClassForElementWithId('stop-mock', 'disabled');
          HTMLElementHandler.addClassForElementWithId('start-mock', 'disabled');

          HTMLElementHandler.addParagraphToElementWithId('log-view-content', 'Mock started');
        }
      } else if (message.type == 'STOP_MOCK_RESPONSE') {
        if (message.content.success == true) {
          this.mockRunning = false;
          HTMLElementHandler.removeClassForElementWithId('start-mock', 'disabled');
          HTMLElementHandler.addClassForElementWithId('stop-mock', 'disabled');
          // enable start button
          // disable stop button
        }
      }

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

  closeAllPopUps() {
		for (let id of this.openedPopUpsIds) {
			this.closePopUp(id);
		}
	}

  closePopUp(popUpId: string) {
		try {
			document.getElementById(popUpId)
							.setAttribute("style", "display: none");
		} catch(ex) { }
  		this.openedPopUpsIds = this.openedPopUpsIds.filter(el => el !== popUpId);
	}

  onPopUpWrapperClick(event) {
		if (event.target.id == 'new-mock-action-pop-up-wrapper') {
			for (let id of this.openedPopUpsIds) {
				this.closePopUp(id);
			}
		}
	}

  onNewMockActionTypeSelected(actionKey) {
    this.project.actions.push(new Action('', actionKey));

    this.closePopUp('new-mock-action-pop-up-wrapper');
    this.closePopUp('select-mock-action-type-view');
  }

  openNewMockActionPopUp() {
    document.getElementById('new-mock-action-pop-up-wrapper')
						.setAttribute("style", "display: block");

    document.getElementById('select-mock-action-type-view')
						.setAttribute('style', 'display: grid');

    this.openedPopUpsIds.push('new-mock-action-pop-up-wrapper');
		this.openedPopUpsIds.push('select-mock-action-type-view');
  }

  startMock() {
    this._crudService.startMock(JSON.stringify(this.project));
  }

  stopMock() {
    this._crudService.stopMock();
  }

  // Intercept shortcuts
	@HostListener('window:keyup', ['$event'])
  keyEvent(event: KeyboardEvent) {
		if (event.key == "F2") {
			//
		} else if (event.key == "Escape") {
			this.closeAllPopUps();
		}
  }
}
