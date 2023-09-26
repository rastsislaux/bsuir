import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {ToastrService} from "ngx-toastr";
import {IWorker} from "../main/main.component";

@Component({
  selector: 'app-payments',
  templateUrl: './payments.component.html',
  styleUrls: ['./payments.component.css']
})
export class PaymentsComponent implements OnInit {

  id: number = 0
  payments: any = []
  worker: IWorker = {grade: "", id: 0, name: "", patronim: "", position: "", surname: "", trade_union: false}

  displayedColumns: string[] = ['datetime', 'payment_id', 'salary', 'surcharge', 'income_tax',
                                'pension_fund', 'trade_union', 'payoff'];

  constructor(private route: ActivatedRoute,
              private http: HttpClient,
              private toast: ToastrService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.id = parseInt(<string>params.get("id"))

      this.http.post("http://localhost:5000/get_worker", { worker_id: this.id }).subscribe({
        next: value => this.worker = <IWorker>value,
        error: err => this.toast.error("Failed to fetch worker.")
      })

      this.http.post("http://localhost:5000/payments", { worker_id: this.id }).subscribe({
        next: value => this.payments = value,
        error: err => this.toast.error("Failed to fetch payments.")
      })
    })
  }

}
