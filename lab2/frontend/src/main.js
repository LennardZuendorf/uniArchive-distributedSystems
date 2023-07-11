import { createApp } from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import App from './App.vue'
import PrimeVue from 'primevue/config';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Toast from 'primevue/toast';
import ToastService from 'primevue/toastservice';

import 'primevue/resources/themes/saga-green/theme.css';
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';
import './assets/main.css'

const app = createApp(App)
// Make PrimeVue available throughout the project
app.use(PrimeVue)
// Install Axios for RESTful Webservice calls
app.use(VueAxios, axios)
// Add primefaces components
app.component('InputText', InputText)
app.component('Button', Button)
app.component('Column', Column)
app.component('DataTable', DataTable)
app.component('Toast', Toast)
app.use(ToastService);
// Start
app.mount('#app')
