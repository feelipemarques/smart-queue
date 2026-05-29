import { Routes } from '@angular/router';
import { TotemComponent } from './components/totem/totem-component/totem-component';

export const routes: Routes = [
    { path: 'totem', component: TotemComponent},
    { path: '', redirectTo: 'totem', pathMatch: 'full'}
];
