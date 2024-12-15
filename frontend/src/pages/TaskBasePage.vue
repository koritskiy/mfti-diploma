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
                            Это страница - часть микросервиса найма сотрудников.
                            На этой странице Вы можете создать задачи, чтобы все остальные могли использовать эти задачи
                            в собеседованиях.
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
                        <a @click="$router.push({path: '/organizations/' + this.organization.id + '/hiring/candidates'})">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2"
                                 stroke="currentColor" class="w-5 h-5">
                                <path stroke-linecap="round" stroke-linejoin="round"
                                      d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z"/>
                            </svg>
                            Все кандидаты
                        </a>
                    </li>
                    <li>
                        <a class="active"
                           @click="$router.push({path: '/organizations/' + this.organization.id + '/hiring/tasks'})">
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
                <li>База задач</li>
            </ul>
        </div>

        <div class="flex gap-5 flex-col lg:flex-row m-10">
            <div class="basis-1/2">
                <div class="card bg-base-200 shadow-xl">
                    <div class="card-body">
                        <h2 class="card-title text-2xl">Создание категории</h2>
                        <div>
                            <success-alert
                                    :is-success="isSuccessCategory"
                                    class="m-0"
                            >Вы успешно создали категорию!
                            </success-alert>
                            <!--                    <success-alert-->
                            <!--                        :is-success="isSuccessTagDelete"-->
                            <!--                    >Вы успешно удалили тег!-->
                            <!--                    </success-alert>-->
                            <error-alert
                                    :is-error="isErrorCategory"
                                    class="m-0"
                            > {{ errorDescriptionCategory }}
                            </error-alert>
                        </div>
                        <create-task-category-form @create="createCategory"/>
                    </div>
                </div>
            </div>
            <div class="basis-1/2">
                <h2 class="mb-5 mt-5 text-2xl font-bold text-base-color text-center"> Все категории </h2>
                <table class="table w-full" v-if="categories.length > 0">
                    <!-- head -->
                    <thead>
                    <tr>
                        <th>Название категории</th>
                        <th>Подробнее</th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr
                            v-for="category in categories"
                            :key="category.id"
                    >
                        <td>
                            <div class="flex items-center space-x-3">
                                <div>
                                    <div class="font-bold">{{ category.name }}</div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <a
                                @click="$router.push('/organizations/' + this.$route.params.id + '/hiring/category/' + category.id)"
                                class="btn gap-2 btn-xs btn-outline"
                            >
                                На страницу категории
                            </a>
                        </td>
                    </tr>
                    </tbody>
                    <!-- foot -->
                    <tfoot>
                    <tr>
                        <th>Название категории</th>
                        <th>Подробнее</th>
                    </tr>
                    </tfoot>
                </table>
                <div class="alert alert-warning shadow-lg" v-else>
                    <div>
                        <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                             viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                        </svg>
                        <span>Пока нет ни одной категории</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="flex justify-center m-10">
            <div class="card w-1/2 bg-base-200 shadow-xl">
                <div class="card-body">
                    <div>
                        <success-alert
                                :is-success="isSuccessTask"
                                class="m-0"
                        >Вы успешно создали задачу!
                        </success-alert>
                        <error-alert
                                :is-error="isErrorTask"
                                class="m-0"
                        > {{ errorDescriptionTask }}
                        </error-alert>
                    </div>
                    <h2 class="card-title text-2xl">
                        Создание задачи
                    </h2>
                    <create-interview-task-form :categories="this.categories" @create="createInterviewTask"/>
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
import CreateTaskCategoryForm from "@/components/CreateTaskCategoryForm.vue";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import Constants from "@/components/constants";
import CreateInterviewTaskForm from "@/components/CreateInterviewTaskForm.vue";

export default {
    components: {CreateInterviewTaskForm, SuccessAlert, ErrorAlert, CreateTaskCategoryForm},
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

            isSuccessCategory: false,
            isErrorCategory: false,
            errorDescriptionCategory: '',

            isSuccessTask: false,
            isErrorTask: false,
            errorDescriptionTask: '',

            categories: []
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
        async getCategories() {
            await axios.get("http://localhost:8080/api/hiring/v1/taskCategory/all/" + this.$route.params.id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.categories = response.data.payload;
            }).catch((response) => {
                console.log(response);
            })
        },
        async createCategory(category) {
            await axios.post("http://localhost:8080/api/hiring/v1/taskCategory/create", {
                name: category.name,
                organizationId: this.$route.params.id
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.isSuccessCategory = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                console.log(response)
                this.isErrorCategory = true;
                this.errorDescriptionCategory = Constants.ERROR_SERVER_DESCRIPTION;
            })
        },
        async createInterviewTask(task) {
            await axios.post("http://localhost:8080/api/hiring/v1/interviewTask/create", {
                name: task.name,
                question: task.question,
                answer: task.answer,
                author: store.state.user.lastName + " " + store.state.user.firstName,
                taskCategoryId: task.taskCategoryId
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.isSuccessTask = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                console.log(response)
                this.isErrorTask = true;
                this.errorDescriptionTask = Constants.ERROR_SERVER_DESCRIPTION;
            })
        }
    },
    mounted() {
        this.getOrganization();
        this.$store.commit('setPageName', 'each-hiring-task-base');
        this.getCategories();
    }
}
</script>