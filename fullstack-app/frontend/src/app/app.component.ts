import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CaseListComponent } from './components/case-list/case-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, CaseListComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Court E-Filing System';
}
