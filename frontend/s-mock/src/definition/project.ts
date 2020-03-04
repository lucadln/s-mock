import { Action } from './action';
import { Guid } from 'guid-typescript';

export class Project {
  private actions: Action[];
  private guid: string;
  private name: string;
  private port: number;
  private path: string;

  constructor() {
    this.guid = Guid.raw();
    this.name = 'Project';
    this.port = 8080;
    this.path = '/'

    this.actions = [];
  }
}
