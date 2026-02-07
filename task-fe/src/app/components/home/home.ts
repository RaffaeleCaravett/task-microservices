import { NgClass } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  effect,
  ElementRef,
  HostListener,
  inject,
  OnInit,
  viewChild,
} from '@angular/core';
import { Router, RouterOutlet, RouterLink } from '@angular/router';
import { ModeService } from '../../services/mode.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import { Tooltip } from "primeng/tooltip";

@Component({
  selector: 'app-home',
  imports: [NgClass, ReactiveFormsModule, RouterLink, Tooltip],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class HomeComponent implements OnInit, AfterViewInit {
  isDark: boolean = false;
  protected modeService: ModeService = inject(ModeService);
  protected cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  protected router: Router = inject(Router);
  homeForm: FormGroup = new FormGroup({});
  protected homeCards: any[] = [];
  canvas = viewChild<ElementRef<HTMLCanvasElement>>('pencil');
  model: any;
  private resizeObserver!: ResizeObserver;
  private pendingResize = false;
  private lastWidth = 0;
  private lastHeight = 0;
  ngOnInit(): void {
    this.onResize(null);
    this.initForm();
    this.initCards();
  }
  ngAfterViewInit(): void {
    const canvasEl = this.canvas();
    if (!canvasEl) return;

    // Scene
    const scene = new THREE.Scene();

    // Camera
    const camera = new THREE.PerspectiveCamera(
      75,
      canvasEl.nativeElement.clientWidth / canvasEl.nativeElement.clientHeight,
      1,
      10,
    );
    camera.position.set(0, 1, 3);

    // Renderer
    const renderer = new THREE.WebGLRenderer({
      canvas: canvasEl.nativeElement,
      antialias: true,
      alpha: true,
    });

    // Lights
    scene.add(new THREE.AmbientLight(0xffffff, 0.8));
    const dirLight = new THREE.DirectionalLight(0xffffff, 1);
    dirLight.position.set(5, 10, 7);
    scene.add(dirLight);

    // GLB loader
    const loader = new GLTFLoader();
    loader.load(
      'assets/models/desk.glb',
      (gltf: any) => {
        this.model = gltf.scene;

        this.model.scale.set(13, 13, 13);
        this.model.rotateY(-7);
        console.log(this.model);
        scene.add(this.model);
      },
      undefined,
      (error: any) => {
        console.error('GLB load error', error);
      },
    );
    const container = canvasEl.nativeElement.parentElement!;

    this.resizeObserver = new ResizeObserver((entries) => {
      const { width, height } = entries[0].contentRect;

      // evita resize inutili
      if (width === this.lastWidth && height === this.lastHeight) return;

      this.lastWidth = width;
      this.lastHeight = height;
      this.pendingResize = true;
    });

    this.resizeObserver.observe(container);
    const animate = () => {
      requestAnimationFrame(animate);
      this.angle += this.speed;
      if (this.model) {
        this.model.position.x = Math.cos(this.angle) * -this.radius;
        this.model.position.z = Math.sin(this.angle) * (this.radius - 0.3);
        this.model.position.y = Math.sin(this.angle) * (this.radius - 0.2);
      }
      if (this.pendingResize) {
        this.pendingResize = false;

        camera.aspect = this.lastWidth / this.lastHeight;
        camera.updateProjectionMatrix();

        renderer.setSize(this.lastWidth, this.lastHeight, false);
      }
      renderer.render(scene, camera);
    };
    animate();
  }
  private angle = 45;
  private radius = 0.5;
  private speed = 0.01;
  initForm() {
    this.homeForm = new FormGroup({
      email: new FormControl('', [
        Validators.required,
        Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/),
      ]),
    });
  }
  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    // let home = document.getElementsByClassName('home')[0] as HTMLDivElement;
    // if (home != null) {
    //   home.style.minHeight = window.innerHeight - (window.innerHeight * 25) / 100 + 'px';
    // }
  }
  checkPermission() {
    if (this.homeForm.valid) {
      this.router.navigate(['signup', this.homeForm.controls['email'].value]);
    }
  }

  initCards() {
    this.homeCards = [
      {
        id: 1,
        title: 'Track',
        description: 'Track everything you want: progresses, tasks, activities and more.',
        icon: 'pi pi-chart-line',
      },
      {
        id: 2,
        title: 'Control',
        description: 'Take control the things that you want. Don\t lose abit of anything.',
        icon: 'pi pi-check-circle',
      },
      {
        id: 3,
        title: 'Delegate',
        description:
          'Assign tasks and more to the people you trust or with the ones you want to share things with.',
        icon: 'pi pi-user-plus',
      },
      {
        id: 4,
        title: 'Organize',
        description: 'Create your own organization. Build your dream team.',
        icon: 'pi pi-sitemap',
      },
    ];
  }
  constructor() {
    effect(() => {
      this.isDark = this.modeService.isDark();
      this.cdr.markForCheck();
    });
  }
}
