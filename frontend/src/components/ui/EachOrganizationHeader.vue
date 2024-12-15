<template>
    <div class="stats shadow stats-vertical lg:stats-horizontal bg-base-200 m-10">
        <div class="stat">
            <div class="stat-title">Название организации</div>
            <div class="stat-value text-primary">{{ organization.name }}</div>
        </div>

        <div class="stat">
            <div class="stat-title">Сайт организации</div>
            <div class="stat-value text-secondary">{{ organization.link }}</div>
        </div>

        <div class="stat" v-if="isCurrentUserOrganizationAdmin">
            <div class="stat-title">Количество сотрудников</div>
            <div class="stat-value text-orange-500">{{ organization.workers.length }}</div>
        </div>

        <div class="stat"
             v-if="isCurrentUserOrganizationAdmin">
            <div class="lg:flex lg:items-center"></div>
            <a @click="$router.push({path: '/organizations/' + this.organization.id + '/workers/create', params: {organization: this.organization}})"
               class="btn btn-outline btn-info">
                Добавить сотрудника
            </a>
        </div>

        <div class="stat">
            <div class="lg:flex lg:items-center"></div>
            <a
                    href="#manage_time"
                    class="btn btn-outline btn-success btn-sm"
            >Управлять рабочим временем</a>
        </div>
    </div>

    <div class="modal" id="manage_time">
        <div class="modal-box">
            <h3 class="font-bold text-lg">Управление рабочим временем</h3>
            <p class="py-4">
                Ниже Вы можете выбрать ту роль, под которой вы сейчас будете работать.
            </p>
            <success-alert
                    class="m-0"
                    :is-success="this.isWorkTimeFinish"
            >Отработанное время сохранено. Вот сколько Вы заработали: <span
                    class="font-bold">{{ this.finalWorkTime.total }}</span>
            </success-alert>
            <div class="flex flex-col">
                <div
                        class="card bg-base-200 text-primary-content w-full mt-5"
                        v-for="func in worker.workerFunctions" :key="func.id"
                >
                    <div class="card-body">
                        <h2 class="card-title">{{ func.functionName }}</h2>
                        <p>
                            <span class="badge badge-info mr-3"> Ставка </span>
                            <span class="font-bold">{{ func.tariffRate }}</span>
                        </p>
                        <p v-if="this.isUserWorking === true && func.functionName === this.currentWorkTime.function.name">
                            <span class="badge badge-info mr-3"> Время начала </span>
                            <span> {{ this.currentWorkTime.startTime }} </span>
                        </p>
                        <div class="card-actions justify-end">
                            <button
                                    class="btn btn-success"
                                    @click="startWorkTime(func.id)"
                                    v-if="this.isUserWorking === false"
                            >
                                Начать
                            </button>
                            <button
                                    class="btn btn-warning"
                                    @click="endWorkTime"
                                    v-else-if="this.isUserWorking === true && func.functionName === this.currentWorkTime.function.name"
                            >
                                Завершить
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-action">
                <a href="#" class="btn">Закрыть</a>
            </div>
        </div>
    </div>
</template>

<script>
import store from "@/store";
import axios from "axios";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";

export default {
    components: {SuccessAlert},
    computed: {
        store() {
            return store
        }
    },
    props: {
        organization: {
            type: Object,
            required: true
        },
        isCurrentUserOrganizationAdmin: {
            type: Boolean,
            required: true
        }
    },
    data() {
        return {
            worker: {
                workerFunctions: []
            },
            isUserWorking: false,
            currentWorkTime: {
                id: 0,
                startTime: '',
                tariffRate: ''
            },
            finalWorkTime: {
                total: 0,
            },
            isWorkTimeFinish: false
        }
    },
    methods: {
        async getWorkerInfo() {
            await axios.get("http://localhost:8080/api/tracker/v1/worker/me", {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.worker = response.data.payload
                console.log(response.data.payload)
            }).catch((response) => {
                console.log(response)
            })
        },
        async getCurrentWorkTime() {
            await axios.get("http://localhost:8080/api/tracker/v1/workTime/get/current", {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                if (response.data.description) {
                    this.isUserWorking = false
                } else {
                    this.isUserWorking = true;
                    this.currentWorkTime = response.data.payload
                }
                console.log(response.data)
            })
        },
        async startWorkTime(functionId) {
            await axios.post("http://localhost:8080/api/tracker/v1/workTime/start", {
                userId: store.state.user.id,
                workerFunctionId: functionId
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.isUserWorking = true;
                this.$router.go(this.$router.currentRoute)
                console.log(response.data.payload)
            }).catch((response) => {
                console.log(response)
            })
        },
        async endWorkTime() {
            await axios.put("http://localhost:8080/api/tracker/v1/workTime/end/" + this.currentWorkTime.id, null, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data)
                this.isUserWorking = false;
                this.finalWorkTime = response.data.payload;
                this.isWorkTimeFinish = true;
            }).catch((response) => {
                console.log(response)
            })
        }
    }, mounted() {
        this.getWorkerInfo();
        this.getCurrentWorkTime();
    }
}
</script>