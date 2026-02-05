import { Component, effect, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavComponent } from './components/nav/nav';
import { FootComponent } from './components/foot/foot';
import { NgClass } from '@angular/common';
import { ModeService } from './services/mode.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavComponent, FootComponent, NgClass],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  protected readonly title = signal('task-fe');
  protected isDark: boolean = false;
  protected modeService: ModeService = inject(ModeService);
  ngOnInit(): void {}

  constructor() {
    effect(() => {
      this.isDark = this.modeService.isDark();
    });
  }
}
