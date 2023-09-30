import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ToastrService} from "ngx-toastr";
import {query} from "@angular/animations";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {

  constructor(private http: HttpClient,
              private toast: ToastrService) {
    this.get_grades()
    this.get_workers()
    this.get_settings()
    this.get_lowest(this.getCurrentMonthAndYear())
  }

  grades: any = [];
  workers: any = [];
  payments: any = [];

  lowest_salary_workers: any = null;

  add_worker_surname: string = "";
  add_worker_name: string = "";
  add_worker_patronim: string = "";
  add_worker_position: string = "";
  add_worker_trade_union: boolean = false;
  add_worker_grade: string = "";

  add_grade_name: string = "";
  add_grade_coefficient: number = 0;

  income_tax: number = 0;
  minimal_payment: number = 0;
  pension_fund: number = 0;
  surcharge: number = 0;
  trade_union: number = 0;
  payday_date: string = this.getCurrentMonthAndYear();
  lowest_month: string = this.getCurrentMonthAndYear();
  query: string = "";

  get_grades() {
    this.http.post("http://localhost:5000/grades", { }).subscribe(
      response => {
        this.grades = response;
        this.add_worker_grade = this.grades[0].name;
      }, error => {
        this.toast.error("Failed to fetch grades.")
      }
    )
  }

  get_workers() {
    let url = "http://localhost:5000/workers?search=" + this.query
    this.http.post(url, { }).subscribe({
      next: value => {
        this.workers = value
      },
      error: err => {
        this.toast.error("Failed to fetch workers.")
      }
    })
  }

  add_worker() {
    this.http.post("http://localhost:5000/add_worker", {
      "surname": this.add_worker_surname,
      "name": this.add_worker_name,
      "patronim": this.add_worker_patronim,
      "position": this.add_worker_position,
      "trade_union": this.add_worker_trade_union,
      "grade": this.add_worker_grade
    }).subscribe({
      next: resp => {
        this.toast.success("Worker added.")
        this.add_worker_name = ""
        this.add_worker_surname = ""
        this.add_worker_patronim = ""
        this.add_worker_grade = ""
        this.add_worker_position = ""
        this.add_worker_trade_union = false
        this.get_workers()
      },
      error: err => {
        this.toast.error(err.error,'', {enableHtml: true});
      }
    })
  }

  delete_worker(id: number) {
    this.http.post("http://localhost:5000/delete_worker", {
      "worker_id": id
    }).subscribe({
      next: value => {
        this.toast.success("Worker deleted.")
        this.get_workers()
      },
      error: err => { this.toast.error("Failed to delete worker.") }
    })

  }

  save_worker(worker: IWorker) {
    this.http.post("http://localhost:5000/edit_worker", worker).subscribe({
      next: value => { this.toast.success("Worker saved successfully.") },
      error: err => { this.toast.error("Failed to save worker.") }
    })
  }

  add_grade() {
    this.http.post("http://localhost:5000/add_grade", {
      name: this.add_grade_name,
      coefficient: this.add_grade_coefficient
    }).subscribe({
      next: value => {
        this.toast.success("Grade saved successfully.")
        this.get_grades()
      },
      error: err => { this.toast.error("Failed to saved grade.") }
    })
  }

  save_grade(grade: any) {
    this.http.post("http://localhost:5000/edit_grade", {
      id: grade.id,
      name: grade.name,
      coefficient: grade.coefficient
    }).subscribe({
      next: value => { this.toast.success("Grade saved successfully.") },
      error: err => { this.toast.error("Failed to save grade.") }
    })
  }

  private get_settings() {
    this.http.post<any>("http://localhost:5000/get_settings", { }).subscribe({
      next: value => {
        this.income_tax = value.income_tax;
        this.minimal_payment = value.minimal_payment;
        this.pension_fund = value.pension_fund;
        this.surcharge = value.surcharge;
        this.trade_union = value.trade_union;
      }
    })
  }

  save_settings() {
    this.http.post("http://localhost:5000/edit_settings", {
      income_tax: this.income_tax,
      minimal_payment: this.minimal_payment,
      pension_fund: this.pension_fund,
      surcharge: this.surcharge,
      trade_union: this.trade_union
    }).subscribe({
      next: value => { this.toast.success("Settings saved successfully."); },
      error: err => { this.toast.error("Failed to saved settings."); }
    })
  }

  get_lowest(month: string) {
    this.http.post<any>("http://localhost:5000/lowest", { "month": month }).subscribe({
      next: value => {
        this.lowest_salary_workers = value
      },
      error: err => this.toast.error("Failed to fetch worker with lowest salary.")
    })
  }

  getCurrentMonthAndYear() {
    const months = [
      'January', 'February', 'March', 'April', 'May', 'June', 'July',
      'August', 'September', 'October', 'November', 'December'
    ];

    const currentDate = new Date();
    const currentMonth = months[currentDate.getMonth()];
    const currentYear = currentDate.getFullYear();

    return `${currentMonth} ${currentYear}`;
  }

  payday() {
    this.http.post("http://localhost:5000/payday", { month: this.payday_date }).subscribe({
      next: value => this.toast.success("Payments successful."),
      error: err => this.toast.error("Failed to make payments.")
    })
  }
}

export interface IWorker {
  id: number
  name: string
  surname: string
  patronim: string
  position: string
  grade: string
  trade_union: boolean
}
