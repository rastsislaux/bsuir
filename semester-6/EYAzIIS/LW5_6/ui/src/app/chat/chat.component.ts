import {Component, ViewChild} from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from "@angular/material/sidenav";
import {MatListItem, MatNavList} from "@angular/material/list";
import {MatButton, MatIconButton, MatMiniFabButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatInput} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MarkdownService} from "ngx-markdown";

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, RouterOutlet, MatSidenavContainer, MatSidenav, MatNavList, MatListItem, MatButton, RouterLink, MatIconButton, MatIcon, MatSidenavContent, MatInput, MatMiniFabButton, FormsModule,
    HttpClientModule, MatProgressSpinner],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {

  title = 'yuri-ui';

  chats = [
    {
      "title": "Какие термины есть в разделе о браке и семье?",
      "messages": [
        "You: Какие термины есть в разделе о браке и семье?",
        "Yuri: брак, близкое родство, брак, брачный договор, воспитание, имущественные права и обязанности родителей, малолетний, многодетная семья, фиктивный брак, усыновление, социально опасное положение ребенка, социально опасное положение, собственность супругов, семья, свойство, приемная семья, детский дом семейного типа, подросток, патронатное воспитание, общая совместная собственность супругов, несовершеннолетний, неполная семья"
      ]
    },
    {
      "title": "Какие понятия на букву с ты знаешь?",
      "messages": [
        "You: Какие понятия на букву с ты знаешь?",
        "Yuri: стандарт, стандартизация, свободное рабочее место (вакансия), собственность супругов, социально опасное положение, социально опасное положение ребенка, специальное жилое помещение, совместное домовладение, свойственники, семья, свойство, стороны трудового договора"
      ]
    },
    {
      "title": "Что говорится в статье 12 кодекса о браке и семье?",
      "messages": [
        "You: Что говорится в статье 12 кодекса о браке и семье?",
        "Yuri: Статья 12. Брак Брак – это добровольный союз женщины и мужчины, который заключается на условиях, предусмотренных настоящим Кодексом, направлен на создание семьи и порождает для сторон взаимные права и обязанности."
      ]
    },
    {
      "title": "Что такое стандарт?",
      "messages": [
        "You: Что такое стандарт?",
        "Yuri:  стандарт – документ, разработанный в процессе стандартизации на основе согласия большинства заинтересованных субъектов технического нормирования и стандартизации и содержащий технические требования к объектам стандартизации"
      ]
    },
    {
      "title": "Фиктивный брак - это?",
      "messages": [
        "You: Фиктивный брак - это?",
        "Yuri: Брак признается недействительным при нарушении условий, установленных статьями 17–19 настоящего Кодекса, а также в случаях регистрации заключения брака без намерения создать семью (фиктивный брак)."
      ]
    }
  ];

  @ViewChild("sidenav") sidenav!: MatSidenav;

  @ViewChild("sendButton") sendButton!: MatButton;

  textInput!: string;

  messages: string[] = [];
  llmMessageHistory: string = "";

  // systemPrompt = "Your name is Yuri and you're a AI lawyer assisstant. " +

  //   "You're a professional in belarusian legislation, and can answer any question about " +
  //   "legislation in Belarus or the world. You're developed by Rastsislau Lipski, " +
  //   "Ilya Zholnerchik, Andrey Strongin, Alexander Protas and Alexey Aleshkevich. " +
  //   "Speak politely and professionaly, use legal terms, and explain them by the way.";
  systemPrompt = "Тебя зовут Юрий и ты - ИИ-ассистент по юриспруденции. Ты -" +
    "профессионал в белорусском законодательстве и можешь ответить на любой вопрос о " +
    "законодательстве Беларуси и всего мира." +
    "Для своей работы ты используешь технологию OSTIS и всю информацию получаешь из базы знаний." +
    "Ты должен общаться ИСКЛЮЧИТЕЛЬНО НА РУССКОМ ЯЗЫКЕ, профессионально и вежливо." +
    "Отвечай максимально кратко и по делу.\n"

  openaiMessageHistory: any[] = [{
    role: 'system',
    content: this.systemPrompt
  }];

  constructor(private http: HttpClient, private mdService: MarkdownService, protected router: Router) {
  }

  async sendChat() {
    this.sendButton.disabled = true;

    if (this.messages.length !== 0) {
      this.llmMessageHistory += "</s><s>\n";
    } else {
      this.llmMessageHistory += "[INST] <<SYS>> " + this.systemPrompt + " <<SYS>> Привет, как тебя зовут? [/INST] Привет, меня зовут Юрий! "
    }

    this.messages.push("You: " + await this.mdService.parse(this.textInput));

    this.llmMessageHistory += "[INST] " + this.textInput + " [/INST] ";
    this.openaiMessageHistory.push({
      role: "user",
      content: this.textInput
    })

    this.http.post(
      "http://localhost:8080",
      this.textInput,
      {
        headers: {
          'Content-Type': 'text/plain; charset=utf-8',
          'Accept': 'text/plain'
        },
        responseType: 'text'
      }
    )
      .subscribe({
        next: async (ostisResponse: any) => {
          this.llmMessageHistory += ostisResponse;
          this.openaiMessageHistory.push({
            role: "assistant",
            content: ostisResponse
          })
          this.messages.push("Yuri: " + ostisResponse);
          this.sendButton.disabled = false;
          this.textInput = "";
        },
        error: () => {
          this.messages.push("Yuri: К сожалению, я не смог найти ответ в базе знаний OSTIS, но постараюсь предоставить ответ " +
            "на основе своих общих знаний, как языковой модели.");
          this.sendChat2();
        }}
      )


  }

  async sendChat2() {
    this.sendButton.disabled = true;
    let body: string = this.llmMessageHistory;

    body += "[INST] ";

    body += this.textInput + "[/INST]";

    this.llmMessageHistory += body;

    const deepinfra_key = '0G7Lme90vjDxWJrdC8gNhSis3pCxAqRv';
    const model_url = 'https://api.deepinfra.com/v1/inference/openchat/openchat_3.5';
    const msgs: any = [{
      role: 'system',
      content: this.systemPrompt
    }];

    for (let message of this.messages) {
      if (message.startsWith("You: ")) {
        msgs.push({
          role: "user",
          content: message.substring(message.indexOf(':') + 2)
        })
      }

      if (message.startsWith("Yuri: ")) {
        msgs.push({
          role: "assistant",
          content: message.substring(message.indexOf(':') + 2)
        })
      }
    }

    this.http.post(
      'https://api.deepinfra.com/v1/openai/chat/completions', {
        model: "microsoft/WizardLM-2-8x22B",
        messages: this.openaiMessageHistory,
        max_tokens: 1000,
      }, {
        headers: {
          Authorization: 'bearer 0G7Lme90vjDxWJrdC8gNhSis3pCxAqRv',
          'Content-Type': 'application/json'
        }
      }
    ).subscribe({
      next: async (value: any) => {
        this.messages.push("Yuri: " + await this.mdService.parse(value.choices[0].message.content));
        this.openaiMessageHistory.push({
          role: "assistant",
          content: value.choices[0].message.content
        })
        this.sendButton.disabled = false;
      },
      error: () => {
        // this.messages.push("Yuri: К сожалению, в данном чате был исчерпан мой лимит, как языковой модели. " +
        //   "Я все ещё могу отвечать на вопросы, ответы на которые можно найти в базе знаний, " +
        //   "но ответить на другие вопросы я не смогу. Попробуйте создать новый чат.")
        this.textInput = "";
        this.sendButton.disabled = false;
      }
    })


    this.textInput = "";
  }

  tipButton(target: any) {
    this.textInput = target;
  }

  newChat() {
    this.messages = [];
  }

  onChatButton(chat: {messages: string[]; title: string}) {
    this.messages = chat.messages;
  }

}
