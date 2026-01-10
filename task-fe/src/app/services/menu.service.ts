import { Injectable, signal, WritableSignal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class MenuService {
  private menu: WritableSignal<{ id: number; value: string }[]> = signal([]);

  constructor() {
    this.menu.set([
      { id: 1, value: 'Projects' },
      { id: 2, value: 'People' },
      { id: 3, value: 'Overview' },
      { id: 4, value: 'Goals' },
      { id: 5, value: 'What to achieve' },
      { id: 6, value: 'What you have achieved' },
      { id: 7, value: 'Motivation' },
      { id: 8, value: 'About us' },
    ]);
  }

  getMenu(): { id: number; value: string }[] {
    return this.menu();
  }
}
