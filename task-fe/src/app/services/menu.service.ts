import { Injectable, signal, WritableSignal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class MenuService {
  private menu: WritableSignal<{ id: number; value: string; icon: string }[]> = signal([]);

  constructor() {
    this.menu.set([
      { id: 1, value: 'Projects', icon: 'pi-bullseye' },
      { id: 2, value: 'People', icon: 'pi-users' },
      { id: 3, value: 'Overview', icon: 'pi-bars' },
      { id: 4, value: 'Goals', icon: 'pi-arrow-up-right' },
      { id: 5, value: 'To achieve', icon: 'pi-list' },
      { id: 6, value: 'Achieved', icon: 'pi-list-check' },
      { id: 7, value: 'Motivation', icon: 'pi-plus' },
      { id: 8, value: 'About us', icon: 'pi-info-circle' },
    ]);
  }

  getMenu(): { id: number; value: string; icon: string }[] {
    return this.menu();
  }
}
