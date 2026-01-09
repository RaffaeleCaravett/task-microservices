import { Injectable, signal, WritableSignal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class MenuService {
  private menu: WritableSignal<string[]> = signal([]);

  constructor() {
    this.menu.set([
      'Projects',
      'People',
      'Overview',
      'Goals',
      'What to achieve',
      'What you have achieved',
      'Motivation',
      'About us',
    ]);
  }

  getMenu(): string[] {
    return this.menu();
  }
}
