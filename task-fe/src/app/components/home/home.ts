import { NgClass } from '@angular/common';
import { ChangeDetectorRef, Component, effect, HostListener, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ModeService } from '../../services/mode.service';

@Component({
  selector: 'app-home',
  imports: [ NgClass],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class HomeComponent implements OnInit {
  isDark: boolean = false;
  protected modeService: ModeService = inject(ModeService);
  protected cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  ngOnInit(): void {
    this.onResize(null);
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    console.log(window.innerHeight);
    let home = document.getElementsByClassName('home')[0] as HTMLDivElement;
    if (home != null) {
      home.style.minHeight = window.innerHeight - (window.innerHeight * 25) / 100 + 'px';
    }
  }

  constructor(){
    effect(()=>{
      this.isDark = this.modeService.isDark();
      this.cdr.markForCheck();
    })
  }
}
