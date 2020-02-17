export class HTMLElementHandler {
  static addClassForElementWithId(id: string, className: string) {
    let el: HTMLElement = document.getElementById(id);
    el.classList.add(className);
  }

  static removeClassForElementWithId(id: string, className: string) {
    console.log('Received command to remove class ' + className + ' from element with id: ' + id);
    let el: HTMLElement = document.getElementById(id);
    el.classList.remove(className);
  }
}
