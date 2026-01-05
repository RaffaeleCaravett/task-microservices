import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class MenuService {
  private readonly menu: BehaviorSubject<string[]> = new BehaviorSubject<string[]>([]);
  public readonly menuObservable: Observable<string[]> = this.menu.asObservable();
  private readonly voice: BehaviorSubject<string> = new BehaviorSubject<string>('');
  public readonly voiceObservable: Observable<string> = this.voice.asObservable();
  public setMenu(menu: string[]) {
    this.menu.next(menu);
  }
  public getMenu(): Observable<string[]> {
    return this.menuObservable;
  }
  public setVoice(voice: string) {
    this.voice.next(voice);
  }
  public getVoice(): Observable<string> {
    return this.voiceObservable;
  }
}
