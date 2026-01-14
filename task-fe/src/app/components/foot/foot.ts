import { Component, effect, inject } from '@angular/core';
import { ModeService } from '../../services/mode.service';
import { NgClass } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-foot',
  imports: [NgClass],
  templateUrl: './foot.html',
  styleUrl: './foot.scss',
})
export class FootComponent {
  isDark: boolean = false;
  protected modeService: ModeService = inject(ModeService);
  protected authService: AuthService = inject(AuthService);
  constructor() {
    effect(() => {
      this.isDark = this.modeService.isDark();
    });
  }
}
