import { Component } from '@angular/core';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from "@angular/material/sidenav";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {NgForOf} from "@angular/common";
import {MatButton} from "@angular/material/button";
import {Router} from "@angular/router";
import {routes} from "../app.routes";

@Component({
  selector: 'app-terms-table',
  standalone: true,
  imports: [
    MatSidenavContainer,
    MatSidenav,
    MatSidenavContent,
    HttpClientModule,
    NgForOf,
    MatButton
  ],
  templateUrl: './terms-table.component.html',
  styleUrl: './terms-table.component.css'
})
export class TermsTableComponent {
  terms: any;

  constructor(private http: HttpClient, private router: Router) {
    http.post("http://localhost:8080/terms", null)
      .subscribe({
        next: value => this.terms = value
      })
  }

  redirectToChat() {
    this.router.navigate(["chat"])
  }
}
