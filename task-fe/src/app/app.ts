import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavComponent } from './components/nav/nav';
import { FootComponent } from './components/foot/foot';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavComponent, FootComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('task-fe');
}
