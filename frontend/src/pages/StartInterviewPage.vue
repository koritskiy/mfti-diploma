<template>
    <div
            class="grid lg:grid-cols-2 mt-10 gap-4 justify-items-center align-middle m-10"
    >
        <div>
            <div class="card bg-base-200 shadow-xl">
                <div class="card-body">
                    <div class="badge badge-primary font-bold">ФИО</div>
                    <h2 class="card-title text-3xl">
                        {{ this.candidate.lastName }} {{ this.candidate.firstName }} {{ this.candidate.middleName }}
                    </h2>
                    <p>
                        Это страница собеседования. Заполняйте все поля по ходу собеседования и сохраняйте результаты.
                        Это позволит посмотреть результаты собеседования в будущем.
                    </p>
                    <div class="alert alert-success shadow-lg mt-3">
                        <div>
                            <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6"
                                 fill="none" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                            </svg>
                            <span>Собеседование уже началось!</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div>
            <ul class="menu bg-base-200 p-2 rounded-box">
                <li class="menu-title">
                    <span>Резюме</span>
                </li>
                <li>
                    <a :href="this.candidate.resumeLink">
                        Перейти
                    </a>
                </li>
                <li class="menu-title">
                    <span>Желаемая должность</span>
                </li>
                <li>
                    <a>{{ this.candidate.position }}</a>
                </li>
                <li class="menu-title">
                    <span>Ожидаемый доход</span>
                </li>
                <li>
                    <a>{{ this.candidate.tariff }}</a>
                </li>
                <li class="menu-title">
                    <span>Telegram</span>
                </li>
                <li>
                    <a>@{{ this.candidate.telegramName }}</a>
                </li>
            </ul>
        </div>
    </div>

    <div class="flex justify-center text-sm breadcrumbs">
        <ul>
            <li><a @click="$router.push('/organizations/' + this.candidate.organizationId)">Компания</a></li>
            <li>Найм сотрудников</li>
            <li>
                <a @click="$router.push({path: '/organizations/' + this.candidate.organizationId + '/hiring/candidates'})">
                    Все кандидаты
                </a>
            </li>
            <li>Собеседование</li>
        </ul>
    </div>

    <div class="grid lg:grid-cols-2 gap-4 justify-items-center align-middle m-10">
        <div class="card bg-base-200 shadow-xl">
            <div class="card-body">
                <h2 class="card-title text-3xl">Помощь в выборе задач</h2>

                <div class="form-control">
                    <label class="label">
                        <span class="label-text">Выберите категорию задач</span>
                    </label>
                    <select
                            class="select select-bordered w-full max-w-xs"
                            @change="getTasksByCategoryId"
                    >
                        <option disabled selected>Выберите категорию</option>
                        <option v-for="category in this.categories" :key="category.id" :value="category.id">
                            {{ category.name }}
                        </option>
                    </select>
                </div>

                <div v-show="this.tasks.length > 0">
                    <ul class="menu bg-base-100 rounded-box mt-5">
                        <div v-for="(t, objKey) in this.tasks" :key="t.id">
                            <li>
                                <a :href="'#each-task-' + t.id">
                                <span class="badge badge-success">
                                    #{{ objKey + 1 }}
                                </span>
                                    {{ t.name }}
                                </a>
                            </li>
                            <div class="modal" :id="'each-task-' + t.id">
                                <div class="modal-box">
                                    <success-alert
                                        :is-success="isSuccessLinkTask"
                                    >Вы успешно привязали задачу к собеседованию!
                                    </success-alert>
                                    <error-alert
                                        :is-error="isErrorLinkTask"
                                    > {{ errorDescriptionLinkTask }}
                                    </error-alert>

                                    <span class="badge badge-success font-bold"> Название задачи </span>
                                    <h3 class="font-bold text-2xl">
                                        {{ t.name }}
                                    </h3>

                                    <p class="py-4">
                                        <span class="font-bold"> Формулировка задачи. </span>
                                        {{ t.question }}
                                    </p>

                                    <p class="py-4">
                                        <span class="font-bold"> Возможный ответ. </span>
                                        {{ t.answer }}
                                    </p>
                                    <div class="modal-action">
                                        <a @click="linkTaskForInterview(t.id)" class="btn btn-sm btn-success">
                                            Привязать к собеседованию
                                        </a>
                                        <a href="#" class="btn btn-sm">Закрыть</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </ul>
                </div>
            </div>
        </div>
        <div class="card bg-base-200 shadow-xl">
            <div class="card-body">
                <success-alert
                    :is-success="isSuccessSaveResult"
                >Вы успешно провели собеседование! Все данные сохранены!
                </success-alert>
                <error-alert
                    :is-error="isErrorSaveResult"
                > {{ errorDescriptionSaveResult }}
                </error-alert>
                <h2 class="card-title text-3xl">Результаты собеседования</h2>
                <save-result-interview-form @create="saveResultOfInterview" />
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import store from "@/store";
import SaveResultInterviewForm from "@/components/SaveResultInterviewForm.vue";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import Constants from "@/components/constants";

export default {
    components: {SuccessAlert, ErrorAlert, SaveResultInterviewForm},
    data() {
        return {
            candidate: '',
            categories: [],
            tasks: [],
            interview: '',

            isSuccessLinkTask: false,
            isErrorLinkTask: false,
            errorDescriptionLinkTask: '',
            isSuccessSaveResult: false,
            isErrorSaveResult: false,
            errorDescriptionSaveResult: ''
        }
    },
    methods: {
        async getCandidate() {
            await axios.get("http://localhost:8080/api/hiring/v1/candidate/" + this.$route.params.candidate_id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.candidate = response.data.payload;
                this.getCategories();
                console.log(response.data.payload);
            }).catch((response) => {
                console.log(response);
            })
        },
        async createInterview() {
            await axios.post("http://localhost:8080/api/hiring/v1/interview/create", {
                recruiter: store.state.user.lastName + " " + store.state.user.firstName,
                candidateId: this.$route.params.candidate_id
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.interview = response.data.payload;
            }).catch((response) => {
                console.log(response.data.payload)
            })
        },
        async getCategories() {
            await axios.get("http://localhost:8080/api/hiring/v1/taskCategory/all/" + this.candidate.organizationId, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log("Categories")
                console.log(response.data.payload)
                this.categories = response.data.payload;
            }).catch((response) => {
                console.log(response);
            })
        },
        async getTasksByCategoryId(e) {
            await axios.get("http://localhost:8080/api/hiring/v1/interviewTask/get/category/" + e.target.value, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload);
                this.tasks = response.data.payload;
            }).catch((response) => {
                console.log(response);
            })
        },
        async linkTaskForInterview(taskId) {
            await axios.post("http://localhost:8080/api/hiring/v1/interview/task/add", {
                interviewId: this.interview.id,
                taskId: taskId
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload);
                this.isSuccessLinkTask = true;
                setTimeout(() => this.isSuccessLinkTask = false, 1000);
            }).catch((response) => {
                console.log(response);
                this.isErrorLinkTask = true;
                this.errorDescriptionLinkTask = Constants.ERROR_SERVER_DESCRIPTION;
            })
        },
        async saveResultOfInterview(interview) {
            await axios.put("http://localhost:8080/api/hiring/v1/interview/result/" + this.interview.id, {
                interviewFeedback: interview.interviewFeedback,
                interviewDecision: interview.interviewDecision
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.isSuccessSaveResult = true;
                setTimeout(() => this.$router.push('/organizations/' + this.candidate.organizationId + '/hiring/candidates'), 1000)
            })
        }
    },
    mounted() {
        this.getCandidate();
        this.createInterview();
    }
}
</script>