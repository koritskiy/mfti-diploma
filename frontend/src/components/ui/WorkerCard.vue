<template>
    <div class="card bg-base-200 shadow-xl lg:w-2/3 m-10">
        <div class="card-body">
            <div class="flex flex-col items-center">
                <success-alert
                    :is-success="isSuccessDismiss"
                >Вы успешно уволили сотрудника!
                </success-alert>
                <error-alert
                    :is-error="isErrorDismiss"
                > {{ errorDescriptionDismiss }}
                </error-alert>
            </div>
            <h2 class="card-title text-3xl">{{ worker.firstName }} {{ worker.lastName }}</h2>
            <div v-if="worker.workerFunctions !== []">
                <p>
                    Ниже представлен список всех привязанных к сотруднику должностей
                </p>
                <div class="lg:flex lg:flex-wrap lg:justify-center">
                    <div v-for="wf in worker.workerFunctions" :key="wf.id">
                        <div class="card bg-base-200 shadow-xl m-10">
                            <div class="card-body">
                                <h2 class="card-title">{{ wf.functionName }}</h2>
                                <p>
                                    <span class="badge badge-info mr-3"> Ставка </span>
                                    <span class="font-bold">{{ wf.tariffRate }}</span>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div v-else>
                <p>
                    У сотрудника пока нет привязанных должностей. Начните уже сейчас!
                </p>
            </div>

            <div
                    v-if="this.organization.ownerUserId === store.state.user.id
                        && this.organization.inn !== null
                        && this.worker.inn !== null"
                    class="flex justify-center"
            >
                <button class="btn gap-2" @click="getDoc()">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                         stroke="currentColor" class="w-6 h-6">
                        <path stroke-linecap="round" stroke-linejoin="round"
                              d="M3 16.5v2.25A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75V16.5M16.5 12L12 16.5m0 0L7.5 12m4.5 4.5V3"/>
                    </svg>

                    Получить квитанцию
                </button>
            </div>

            <div class="card-actions justify-end">
                <a @click="dismissWorker" class="btn btn-error btn-sm">
                    Уволить сотрудника
                </a>
            </div>
        </div>
    </div>
</template>

<script>
import store from "@/store";
import axios from "axios";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import Constants from "@/components/constants";

export default {
    components: {SuccessAlert, ErrorAlert},
    computed: {
        store() {
            return store
        }
    },
    props: {
        worker: {
            type: Object,
            required: true
        },
        organization: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            isSuccessDismiss: false,
            isErrorDismiss: false,
            errorDescriptionDismiss: ''
        }
    },
    methods: {
        async getDoc() {
            await axios.post("http://localhost:8080/api/tracker/v1/doc/get", {
                organizationId: this.organization.id,
                workerId: this.worker.id,
                reason: "Зачисление заработной платы."
            }, {
                responseType: "blob",
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                window.open(URL.createObjectURL(response.data));
            }).catch((response) => {
                console.log(response)
            })
        },
        async dismissWorker() {
            await axios.put("http://localhost:8080/api/tracker/v1/worker/dismiss", {
                organizationId: this.organization.id,
                workerId: this.worker.id
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.isSuccessDismiss = true;
            }).catch((response) => {
                console.log(response);
                this.isErrorDismiss = true;
                this.errorDescriptionDismiss = Constants.ERROR_SERVER_DESCRIPTION;
            })
        }
    }
}
</script>