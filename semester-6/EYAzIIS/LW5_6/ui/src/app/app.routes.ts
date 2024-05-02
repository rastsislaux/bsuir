import { Routes } from '@angular/router';
import {ChatComponent} from "./chat/chat.component";
import {TermsTableComponent} from "./terms-table/terms-table.component";

export const routes: Routes = [
  { path: "chat", component: ChatComponent },
  { path: "terms", component: TermsTableComponent },
  { path: "**", redirectTo: "chat" }
];
