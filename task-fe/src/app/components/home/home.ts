import { Component, HostListener, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [RouterOutlet],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class HomeComponent implements OnInit {
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
}
