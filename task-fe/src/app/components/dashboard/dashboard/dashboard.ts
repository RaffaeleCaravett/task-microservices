import { AfterViewInit, ChangeDetectorRef, Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MenuService } from '../../../services/menu.service';
import { filter } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  imports: [RouterOutlet],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class DashboardComponent implements AfterViewInit {
  protected menuVoices: string[] = [];
  protected menuService: MenuService = inject(MenuService);
  protected cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  constructor() {}
  ngAfterViewInit(): void {
  }
}
