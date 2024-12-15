<template>
    <div class="flex justify-center text-sm breadcrumbs mt-10">
        <ul>
            <li><a @click="$router.push('/organizations/' + $route.params.id)">Компания</a></li>
            <li>Добавление сотрудника</li>
        </ul>
    </div>
    <div class="flex flex-col items-center">
        <success-alert
                class="w-1/2"
                :is-success="isSuccess"
        >Вы успешно создали сотрудника!
        </success-alert>
        <error-alert
                class="w-1/2"
                :is-error="isError"
        > {{ errorDescription }}
        </error-alert>
    </div>
    <div class="flex justify-center">
        <add-worker-form @create="createWorker"/>
    </div>
</template>

<script>
import AddWorkerForm from "@/components/AddWorkerForm.vue";
import axios from "axios";
import store from "@/store";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import Constants from "@/components/constants";

export default {
    components: {SuccessAlert, ErrorAlert, AddWorkerForm},
    data() {
        return {
            isSuccess: false,
            isError: false,
            errorDescription: ''
        }
    },
    methods: {
        async createWorker(worker) {
            if (worker.email === '' || worker.firstName === '' || worker.lastName === '') {
                this.isError = true;
                this.errorDescription = Constants.ERROR_EMPTY_FIELDS;
                return
            }
            await axios.post('http://localhost:8080/api/tracker/v1/worker/create', {
                email: worker.email,
                name: worker.firstName,
                surname: worker.lastName,
                organizationId: this.$route.params.id
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then(() => {
                this.isSuccess = true;
                let organizationId = this.$route.params.id;
                setTimeout(() => this.$router.push('/organizations/' + organizationId), 1000);
            }).catch((response) => {
                this.isError = true;
                this.errorDescription = Constants.ERROR_SERVER_DESCRIPTION;
                console.log(response);
            })
        }
    },
    mounted() {
        this.$store.commit('setPageName', 'add-worker-page');
    }
}
</script>