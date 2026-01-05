import { AfterViewInit, ChangeDetectorRef, Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MenuService } from '../../../services/menu.service';
import { filter } from 'rxjs';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  imports: [RouterOutlet, NgClass],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class DashboardComponent implements AfterViewInit {
  protected menuVoices: string[] = [];
  protected menuService: MenuService = inject(MenuService);
  protected cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  constructor() {}
  ngAfterViewInit(): void {
    this.menuService
      .getMenu()
      .pipe(filter((data: string[]) => data.length > 0))
      .subscribe({
        next: (data: string[]) => {
          this.menuVoices = data;
          this.cdr.detectChanges();
        },
      });
  }

  next(value: string) {
    this.menuService.setVoice(value);
  }
}
