import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import {MatTab, MatTabGroup} from "@angular/material/tabs";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {FormsModule} from "@angular/forms";
import {MatAccordion, MatExpansionPanel, MatExpansionPanelHeader} from "@angular/material/expansion";
import {MatInput} from "@angular/material/input";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, MatTabGroup, MatTab, HttpClientModule, MatIconButton, MatIcon, MatButton, MatFormField, MatSelect, MatOption, MatLabel, FormsModule, MatAccordion, MatExpansionPanel, MatExpansionPanelHeader, MatInput],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'ui';
  classes: any;
  ops: any;
  selectedClazz: any;
  pps: any;
  inds: any;
  selectedClazzForNewOp: any;
  availableOps: any;
  selectedObjectProperty: any;
  ontologyURL: any;
  inputString: any;
  sparqlQuery: string = "";
  sparqlResult: any = "";

  constructor(private http: HttpClient) {
    this.findClasses()
    this.updateAvailableOps()
  }

  create() {
    this.http.post(`http://localhost:8080/class?name=${prompt("Enter name:")}`, {})
      .subscribe({
        next: value => this.findClasses(),
        error: err => console.error(err)
      })
  }

  findClasses() {
    this.http.get("http://localhost:8080/class").subscribe({
      next: value => { this.classes = value; },
      error: err => { console.log(err)}
    })
  }

  delete(clazz: any) {
    this.http.post(`http://localhost:8080/class/delete`, clazz, {
      headers: {
        'Content-Type': 'text/plain; charset=utf-8'
      }
    }).subscribe({
      next: value => this.findClasses(),
      error: err => { console.log(err) }
    })
  }

  updateObjectProperties() {
    this.http.post("http://localhost:8080/relations/for", this.selectedClazz, {
      headers: {
        'Content-Type': 'text/plain; charset=utf-8'
      }
    }).subscribe({
      next: value => this.ops = value,
      error: err => console.error(err)
    })
  }

  updatePrimitiveProperties() {
    this.http.post("http://localhost:8080/primitive/for", this.selectedClazz, {
      headers: {
        'Content-Type': 'text/plain; charset=utf-8'
      }
    }).subscribe({
      next: value => this.pps = value,
      error: err => console.error(err)
    })
  }

  updateAvailableOps() {
    this.http.post("http://localhost:8080/relations/all", null, {}).subscribe({
      next: value => this.availableOps = value,
      error: err => console.error(err)
    })
  }

  updateIndividuals() {
    this.http.post("http://localhost:8080/individuals/for", this.selectedClazz, {
      headers: {
        'Content-Type': 'text/plain; charset=utf-8'
      }
    }).subscribe({
      next: value => this.inds = value,
      error: err => console.error(err)
    })
  }

  addOPToEntity() {
    this.http.post("http://localhost:8080/relations/create", {
      subj: this.selectedClazz,
      pred: this.selectedObjectProperty,
      obj: this.selectedClazzForNewOp
    }).subscribe({
      next: value => this.updateObjectProperties(),
      error: err => console.error(err)
    })
  }

  loadOntology(ontologyURL: any) {
    this.http.post("http://localhost:8080/ont/read", ontologyURL, {
      headers: {
        'Content-Type': 'text/plain; charset=utf-8'
      }
    }).subscribe({
      next: value => {
        this.findClasses()
        this.updateObjectProperties()
        this.updatePrimitiveProperties()
        this.updateIndividuals()
        this.updateAvailableOps()
      }
    })
  }

  clearOntology() {
    this.http.post("http://localhost:8080/ont/clear", null).subscribe({
      next: value => {
        this.findClasses()
        this.updateObjectProperties()
        this.updatePrimitiveProperties()
        this.updateIndividuals()
        this.updateAvailableOps()
      }
    })
  }

  deleteOp(selectedClazz: any, prName: any, obj: any) {
    this.http.post("http://localhost:8080/relations/delete", {
      subj: selectedClazz,
      pred: prName,
      obj: obj
    }).subscribe({
      next: obj => this.updateObjectProperties(),
      error: err => console.error(err)
    })
  }

  saveOntology() {
    this.http.post("http://localhost:8080/ont/save", null, {
      responseType: 'blob'
    }).subscribe({
      next: value => {
        this.downloadURI(window.URL.createObjectURL(value), "ontology.owl");
      },
      error: err => console.error(err)
    })
  }

  downloadURI(uri: string, name: string) {
    let link = document.createElement("a");
    link.download = name;
    link.href = uri;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  deletePP(selectedClazz: any, prName: any) {
    this.http.post("http://localhost:8080/primitive/delete", {
      subj: selectedClazz,
      pred: prName
    }).subscribe({
      next: value => this.updatePrimitiveProperties(),
      error: err => console.error(err)
    })
  }

  addPPToEntity(selectedClazz: any, selectedObjectProperty: any, inputString: any) {
    this.http.post("http://localhost:8080/primitive/create", {
      subj: selectedClazz,
      pred: selectedObjectProperty,
      obj: inputString
    }).subscribe({
      next: value => this.updatePrimitiveProperties(),
      error: err => console.error(err)
    })
  }

  createIndividual(inputString: any) {
    this.http.post("http://localhost:8080/individuals/create", {
      clazz: this.selectedClazz,
      name: this.inputString
    }).subscribe({
      next: value => this.updateIndividuals(),
      error: err => console.error(err)
    })
  }

  deleteIndividual(url: any) {
    this.http.post("http://localhost:8080/individuals/delete", {
      url: url
    }).subscribe({
      next: value => this.updateIndividuals(),
      error: err => console.error(err)
    })
  }

  executeSparql() {
    this.http.post("http://localhost:8080/sparql", this.sparqlQuery, {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      },
      responseType: 'text'
    }).subscribe({
      next: value => this.sparqlResult = value,
    })
  }
}
