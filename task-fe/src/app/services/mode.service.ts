import { Injectable, signal, WritableSignal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ModeService {
  public isDark: WritableSignal<boolean> = signal(true);

  public toggleMode() {
    this.isDark.set(!this.isDark());
  }
}
