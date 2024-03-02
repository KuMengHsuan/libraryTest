import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import PrimeVue from 'primevue/config';
import 'primevue/resources/themes/aura-light-green/theme.css'
import InputText from 'primevue/inputtext';
import FloatLabel from 'primevue/floatlabel';
import Button from 'primevue/button';
import axios from 'axios';

const app = createApp(App).use(router);
app.use(PrimeVue, { ripple: true });
app.use(axios)
app.component('InputText', InputText);
app.component('FloatLabel', FloatLabel);
app.component('Button', Button);

app.mount('#app')