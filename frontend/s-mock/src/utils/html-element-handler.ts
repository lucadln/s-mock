export class HTMLElementHandler {
  static addClassForElementWithId(id: string, className: string) {
    let el: HTMLElement = document.getElementById(id);
    el.classList.add(className);
  }

  static removeClassForElementWithId(id: string, className: string) {
    let el: HTMLElement = document.getElementById(id);
    el.classList.remove(className);
  }

  static addParagraphToElementWithId(id: string, paragraphText: string) {
    let para = document.createElement("p");
    let node = document.createTextNode(paragraphText);
    para.appendChild(node);
    document.getElementById(id).appendChild(para);
  }
}
