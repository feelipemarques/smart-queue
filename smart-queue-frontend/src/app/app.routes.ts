import { Routes } from '@angular/router';
import { TotemComponent } from './components/totem/totem-component/totem-component';
import { AttendantComponent } from './components/attendant/attendant-component/attendant-component';

export const routes: Routes = [
    { path: 'totem', component: TotemComponent},
    { path: '', redirectTo: 'totem', pathMatch: 'full'},
    { path: 'attendant', component: AttendantComponent}
];
