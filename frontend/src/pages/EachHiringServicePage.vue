<template>
    <div v-if="this.isCurrentUserOrganizationAdmin">
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
                        <a class="active" @click="$router.push({path: '/organizations/' + this.organization.id + '/hiring'})">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2"
                                 stroke="currentColor" class="w-5 h-5">
                                <path stroke-linecap="round" stroke-linejoin="round"
                                      d="M19 7.5v3m0 0v3m0-3h3m-3 0h-3m-2.25-4.125a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zM4 19.235v-.11a6.375 6.375 0 0112.75 0v.109A12.318 12.318 0 0110.374 21c-2.331 0-4.512-.645-6.374-1.766z"/>
                            </svg>
                            Создать нового кандидата
                        </a>
                    </li>
                    <li>
                        <a @click="$router.push({path: '/organizations/' + this.organization.id + '/hiring/candidates'})">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-5 h-5">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z" />
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
                        <a href="#hiring_stats">
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
                <li>Добавить кандидата</li>
            </ul>
        </div>

        <div class="flex justify-center m-10">
            <div class="card bg-base-200 shadow-xl">
                <div class="card-body">
                    <div class="flex flex-col items-center">
                        <success-alert
                                :is-success="isSuccessCandidate"
                        >Вы успешно создали кандидата!
                        </success-alert>
                        <error-alert
                                :is-error="isErrorCandidate"
                        > {{ errorDescriptionCandidate }}
                        </error-alert>
                    </div>
                    <h2 class="card-title">Завести нового кандидата</h2>
                    <create-candidate-form @create="createCandidate" />
                </div>
            </div>
        </div>

        <div class="flex flex-col justify-center m-10" id="hiring_stats">
            <div>
                <h2 class="mb-5 mt-5 text-2xl font-bold text-base-color text-center">
                    Статистика сервиса найма
                </h2>
            </div>
            <div class="stats stats-vertical lg:stats-horizontal shadow bg-base-200">
                <div class="stat">
                    <div class="stat-figure text-secondary">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-8 h-8">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z" />
                        </svg>
                    </div>
                    <div class="stat-title">Всего кандидатов</div>
                    <div class="stat-value">{{ this.stats.countCandidates }}</div>
                </div>

                <div class="stat">
                    <div class="stat-figure text-secondary">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-8 h-8">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M12 18.75a6 6 0 006-6v-1.5m-6 7.5a6 6 0 01-6-6v-1.5m6 7.5v3.75m-3.75 0h7.5M12 15.75a3 3 0 01-3-3V4.5a3 3 0 116 0v8.25a3 3 0 01-3 3z" />
                        </svg>
                    </div>
                    <div class="stat-title">Всего интервью</div>
                    <div class="stat-value">{{ this.stats.countInterviews }}</div>
                </div>

                <div class="stat">
                    <div class="stat-figure text-secondary">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-8 h-8">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M15.182 16.318A4.486 4.486 0 0012.016 15a4.486 4.486 0 00-3.198 1.318M21 12a9 9 0 11-18 0 9 9 0 0118 0zM9.75 9.75c0 .414-.168.75-.375.75S9 10.164 9 9.75 9.168 9 9.375 9s.375.336.375.75zm-.375 0h.008v.015h-.008V9.75zm5.625 0c0 .414-.168.75-.375.75s-.375-.336-.375-.75.168-.75.375-.75.375.336.375.75zm-.375 0h.008v.015h-.008V9.75z" />
                        </svg>
                    </div>
                    <div class="stat-title">Кандидатов без интервью</div>
                    <div class="stat-value">{{ this.stats.countCandidatesWithoutInterview }}</div>
                </div>

                <div class="stat">
                    <div class="stat-figure text-secondary">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-8 h-8">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M9 12h3.75M9 15h3.75M9 18h3.75m3 .75H18a2.25 2.25 0 002.25-2.25V6.108c0-1.135-.845-2.098-1.976-2.192a48.424 48.424 0 00-1.123-.08m-5.801 0c-.065.21-.1.433-.1.664 0 .414.336.75.75.75h4.5a.75.75 0 00.75-.75 2.25 2.25 0 00-.1-.664m-5.8 0A2.251 2.251 0 0113.5 2.25H15c1.012 0 1.867.668 2.15 1.586m-5.8 0c-.376.023-.75.05-1.124.08C9.095 4.01 8.25 4.973 8.25 6.108V8.25m0 0H4.875c-.621 0-1.125.504-1.125 1.125v11.25c0 .621.504 1.125 1.125 1.125h9.75c.621 0 1.125-.504 1.125-1.125V9.375c0-.621-.504-1.125-1.125-1.125H8.25zM6.75 12h.008v.008H6.75V12zm0 3h.008v.008H6.75V15zm0 3h.008v.008H6.75V18z" />
                        </svg>
                    </div>
                    <div class="stat-title">Категорий задач</div>
                    <div class="stat-value">{{ this.stats.countTaskCategories }}</div>
                </div>


                <div class="stat">
                    <div class="stat-figure text-secondary">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-8 h-8">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M6 6.878V6a2.25 2.25 0 012.25-2.25h7.5A2.25 2.25 0 0118 6v.878m-12 0c.235-.083.487-.128.75-.128h10.5c.263 0 .515.045.75.128m-12 0A2.25 2.25 0 004.5 9v.878m13.5-3A2.25 2.25 0 0119.5 9v.878m0 0a2.246 2.246 0 00-.75-.128H5.25c-.263 0-.515.045-.75.128m15 0A2.25 2.25 0 0121 12v6a2.25 2.25 0 01-2.25 2.25H5.25A2.25 2.25 0 013 18v-6c0-.98.626-1.813 1.5-2.122" />
                        </svg>
                    </div>
                    <div class="stat-title">Всего задач</div>
                    <div class="stat-value">{{ this.stats.countTasks }}</div>
                </div>

            </div>
        </div>
    </div>

    <div v-else>
        <div class="alert alert-error shadow-lg">
            <div>
                <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                     viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                </svg>
                <span>У Вас недостаточно прав на просмотр этой страницы.</span>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import store from "@/store";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import CreateCandidateForm from "@/components/CreateCandidateForm.vue";
import Constants from "@/components/constants";

export default {
    components: {CreateCandidateForm, SuccessAlert, ErrorAlert},
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
            isCurrentUserOrganizationAdmin: false,

            isSuccessCandidate: false,
            isErrorCandidate: false,
            errorDescriptionCandidate: '',

            stats: {
                countCandidates: 0,
                countCandidatesWithoutInterview: 0,
                countInterviews: 0,
                countTaskCategories: 0,
                countTasks: 0
            }
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
                this.checkIsCurrentUserAdmin();
                this.getHiringStats();
                console.log(response.data.payload)
                console.log(store.state.token)
            }).catch((error) => {
                console.log(error)
            })
        },
        checkIsCurrentUserAdmin() {
            if (store.state.user.isUserAdmin === true) {
                this.isCurrentUserOrganizationAdmin = true;
            }

            if (this.organization.ownerUserId === store.state.user.id) {
                this.isCurrentUserOrganizationAdmin = true;
            }

            for (let i = 0; i < this.organization.workers.length; i++) {
                if (this.organization.workers[i].userId === store.state.user.id && this.organization.workers[i].isOrganizationAdmin === true) {
                    this.isCurrentUserOrganizationAdmin = true
                }
            }
        },
        async createCandidate(candidate) {
            await axios.post("http://localhost:8080/api/hiring/v1/candidate/create", {
                firstName: candidate.firstName,
                middleName: candidate.middleName,
                lastName: candidate.lastName,
                email: candidate.email,
                organizationId: this.organization.id,
                resumeLink: candidate.resumeLink,
                position: candidate.position,
                tariff: candidate.tariff,
                phone: candidate.phone,
                telegramName: candidate.telegramName
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.isSuccessCandidate = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                console.log(response)
                this.isErrorCandidate = true;
                if (response.response.status === 409) {
                    this.errorDescriptionCandidate = "Этот кандидат уже есть в Вашей компании. Попробуйте найти его в списке кандидатов.";
                } else {
                    this.errorDescriptionCandidate = Constants.ERROR_SERVER_DESCRIPTION;
                }
            })
        },
        async getHiringStats() {
            await axios.get("http://localhost:8080/api/hiring/v1/candidate/stats/" + this.organization.id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload);
                this.stats = response.data.payload;
            }).catch((response) => {
                console.log(response);
            })
        }
    },
    mounted() {
        this.getOrganization();
        this.$store.commit('setPageName', 'each-hiring-service');
    }
}
</script>