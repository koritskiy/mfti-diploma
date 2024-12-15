<template>
    <div
            class="grid lg:grid-cols-2 mt-10 gap-4 justify-items-center m-10"
    >
        <div>
            <div class="card bg-base-200 shadow-xl">
                <div class="card-body">
                    <div class="badge badge-primary font-bold">Название компании</div>
                    <h2 class="card-title text-3xl">
                        Найм сотрудников в компанию {{ this.organization.name }}
                    </h2>
                    <p>
                        Это страница микросервиса найма сотрудников.
                        С помощью него Вы можете заводить профили кандидатов и вести свою базу заданий, а также
                        переводить
                        кандидатов в полноценных сотрудников компании.
                    </p>
                </div>
            </div>
        </div>

        <div>
            <ul class="menu bg-base-200 p-2 rounded-box">
                <li>
                    <a @click="$router.push({path: '/organizations/' + this.organization.id + '/hiring'})">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2"
                             stroke="currentColor" class="w-5 h-5">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                  d="M19 7.5v3m0 0v3m0-3h3m-3 0h-3m-2.25-4.125a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zM4 19.235v-.11a6.375 6.375 0 0112.75 0v.109A12.318 12.318 0 0110.374 21c-2.331 0-4.512-.645-6.374-1.766z"/>
                        </svg>
                        Создать нового кандидата
                    </a>
                </li>
                <li>
                    <a @click="$router.push({path: '/organizations/' + this.organization.id + '/hiring/candidates'})"
                       class="active">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2"
                             stroke="currentColor" class="w-5 h-5">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                  d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z"/>
                        </svg>
                        Все кандидаты
                    </a>
                </li>
                <li>
                    <a @click="$router.push({path: '/organizations/' + this.organization.id + '/hiring/tasks'})">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2"
                             stroke="currentColor" class="w-5 h-5">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                  d="M9 12h3.75M9 15h3.75M9 18h3.75m3 .75H18a2.25 2.25 0 002.25-2.25V6.108c0-1.135-.845-2.098-1.976-2.192a48.424 48.424 0 00-1.123-.08m-5.801 0c-.065.21-.1.433-.1.664 0 .414.336.75.75.75h4.5a.75.75 0 00.75-.75 2.25 2.25 0 00-.1-.664m-5.8 0A2.251 2.251 0 0113.5 2.25H15c1.012 0 1.867.668 2.15 1.586m-5.8 0c-.376.023-.75.05-1.124.08C9.095 4.01 8.25 4.973 8.25 6.108V8.25m0 0H4.875c-.621 0-1.125.504-1.125 1.125v11.25c0 .621.504 1.125 1.125 1.125h9.75c.621 0 1.125-.504 1.125-1.125V9.375c0-.621-.504-1.125-1.125-1.125H8.25zM6.75 12h.008v.008H6.75V12zm0 3h.008v.008H6.75V15zm0 3h.008v.008H6.75V18z"/>
                        </svg>
                        База задач
                    </a>
                </li>
                <li>
                    <a @click="$router.push({path: '/organizations/' + this.organization.id + '/hiring'})">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                             stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/>
                        </svg>
                        Статистика найма сотрудников
                    </a>
                </li>
            </ul>
        </div>
    </div>

    <div class="flex justify-center text-sm breadcrumbs">
        <ul>
            <li><a @click="$router.push('/organizations/' + organization.id)">Компания</a></li>
            <li>Найм сотрудников</li>
            <li>Все кандидаты</li>
        </ul>
    </div>

    <div class="flex flex-row flex-wrap">
        <div
                class="lg:basis-1/4 w-full m-10"
                v-for="c in candidates" :key="c.id"
        >
            <div class="card bg-base-200 shadow-xl">
                <div class="card-body">
                    <div class="flex flex-col items-center">
                        <success-alert
                            :is-success="isSuccessMove"
                        >Вы успешно приняли кандидата на работу!
                        </success-alert>
                        <error-alert
                            class="w-1/2"
                            :is-error="isErrorMove"
                        > {{ errorDescriptionMove }}
                        </error-alert>
                    </div>
                    <span v-if="c.interviews.length === 0" class="badge badge-success font-bold">Новый кандидат</span>
                    <span v-if="c.isHired === true" class="badge badge-success font-bold">Кандидат принят на работу</span>
                    <h2 class="card-title">{{ c.lastName }} {{ c.firstName }} {{ c.middleName }}</h2>
                    <div>
                        <span class="badge mr-3 font-bold">{{ c.phone }}</span>
                        <span class="badge mr-3 font-bold">@{{ c.telegramName }}</span>
                    </div>
                    <div v-if="c.interviews.length === 0">
                        <p>
                            С этим кандидатом не проводили еще ни одного собеседования. Проводите скорее!
                        </p>
                        <div class="card-actions justify-end mt-3">
                            <a :href="'#start-interview-' + c.id" class="btn btn-primary btn-sm">
                                Начать собеседование
                            </a>
                        </div>
                    </div>
                    <div v-else>
                        <p>
                            С этим кандидатом проводили собеседования. Вы можете посмотреть их результаты или провести еще одно!
                        </p>
                        <div class="card-actions justify-end mt-3">
                            <a :href="'#start-interview-' + c.id" class="btn btn-primary btn-sm" v-show="c.isHired === false">
                                Начать собеседование
                            </a>
                            <a :href="'#history-' + c.id" class="btn btn-sm">
                                Посмотреть историю
                            </a>
                            <a @click="moveCandidateToWorker(c.id)" class="btn btn-sm btn-success" v-show="c.isHired === false">
                                Взять на работу
                            </a>

                            <div :id="'history-' + c.id" class="modal">
                                <div class="modal-box">
                                    <h3 class="font-bold text-lg">
                                        История собеседований с кандидатом
                                    </h3>

                                    <div class="flex flex-col justify-center">
                                        <ul v-for="interview in c.interviews" :key="interview.id"
                                            class="menu bg-base-200 p-2 rounded-box mt-5"
                                        >
                                            <li>
                                                <a class="active">Собеседование №{{interview.id}}</a>
                                            </li>
                                            <li class="menu-title">
                                                <span>Общее впечатление</span>
                                            </li>
                                            <li>
                                                <a>{{ interview.interviewFeedback }}</a>
                                            </li>
                                            <li class="menu-title">
                                                <span>Финальное решение</span>
                                            </li>
                                            <li>
                                                <a>{{ interview.interviewDecision }}</a>
                                            </li>
                                        </ul>
                                    </div>

                                    <div class="modal-action">
                                        <a href="#" class="btn">Закрыть</a>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
                <div class="modal" :id="'start-interview-' + c.id">
                    <div class="modal-box">
                        <h3 class="font-bold text-lg">
                            Подтвердите начало собеседования
                        </h3>
                        <p class="py-4">
                            После подтверждения начала собеседования Вы сразу сможете формировать задачи
                            и сохранить финальное решение в систему.
                        </p>
                        <div class="modal-action">
                            <a @click="$router.push('/hiring/candidates/interview/start/' + c.id)" class="btn btn-success">
                                Начать
                            </a>
                            <a href="#" class="btn">Закрыть</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

import {defineComponent} from "vue";
import axios from "axios";
import store from "@/store";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import Constants from "@/components/constants";

export default defineComponent({
    components: {SuccessAlert, ErrorAlert},
    data() {
        return {
            organization: {
                id: 0,
                name: '',
                address: '',
                link: '',
                workers: [],
                functions: [],
                taskBoards: []
            },
            candidates: [],

            isSuccessMove: false,
            isErrorMove: false,
            errorDescriptionMove: ''
        }
    },
    methods: {
        async getOrganization() {
            await axios.get("http://localhost:8080/api/tracker/v1/organization/getById/" + this.$route.params.id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.organization = response.data.payload;
                console.log(response.data.payload)
                console.log(store.state.token)
            }).catch((error) => {
                console.log(error)
            })
        },
        async getCandidates() {
            await axios.get("http://localhost:8080/api/hiring/v1/candidate/get/" + this.$route.params.id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.candidates = response.data.payload;
            }).catch((response) => {
                console.log(response)
            })
        },
        async moveCandidateToWorker(candidateId) {
            await axios.put("http://localhost:8080/api/hiring/v1/candidate/move/worker/" + candidateId, null, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload);
                this.isSuccessMove = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                console.log(response);
                this.isErrorMove = true;
                this.errorDescriptionMove = Constants.ERROR_SERVER_DESCRIPTION;
            })
        }
    },
    mounted() {
        this.getOrganization();
        this.getCandidates();
    }
})
</script>