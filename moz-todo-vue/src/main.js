import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import PrimeVue from 'primevue/config';
import 'primevue/resources/themes/aura-light-green/theme.css'
import InputText from 'primevue/inputtext';
import FloatLabel from 'primevue/floatlabel';
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import ColumnGroup from 'primevue/columngroup';   // optional
import Row from 'primevue/row';                   // optional



const app = createApp(App).use(router);
app.use(PrimeVue, { ripple: true });


app.component('InputText', InputText);
app.component('FloatLabel', FloatLabel);
app.component('Button', Button);
app.component('DataTable', DataTable);
app.component('Column', Column);
app.component('ColumnGroup', ColumnGroup);
app.component('Row', Row);

app.mount('#app')