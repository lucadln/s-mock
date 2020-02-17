import { Guid } from 'guid-typescript';

export class Action {
  guid: string;
  name: string;
  type: string;

  constructor(name: string, type: string) {
    this.guid = Guid.raw();

    this.name = name;
    this.type = type;
  }
}
