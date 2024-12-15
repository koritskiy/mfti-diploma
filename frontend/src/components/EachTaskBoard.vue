<template>
    <div class="flex justify-center">
        <each-organization-header
                :organization="organization"
                :is-current-user-organization-admin="this.isCurrentUserOrganizationAdmin"/>
    </div>

    <div class="flex justify-center text-sm breadcrumbs">
        <ul>
            <li><a @click="$router.push('/organizations/' + organization.id)">Компания</a></li>
            <li>{{ board.name }}</li>
        </ul>
    </div>

    <div class="grid grid-flow-col auto-cols-max gap-20 overflow-x-auto ml-10 mt-10 items-center">
        <div v-for="column in board.boardColumns" :key="column.id">
            <div>
                <div class="text-center">
                    <div class="badge badge-primary badge-lg">{{ column.name }}</div>
                </div>
                <div v-if="column.tasks.length > 0">
                    <div class="flex flex-col gap-y-5">
                        <div
                                v-for="task in column.tasks"
                                :key="task.id"
                                class="card w-96 bg-base-100 shadow-xl">
                            <div class="card-body">
                                <h2 class="card-title">{{ task.name }}</h2>
                                <p>
                                    {{ task.description }}
                                </p>
                                <div class="card-actions justify-end mt-5">
                                    <a @click="this.$router.push('/organizations/' + this.organization.id + '/boards/get/' + this.board.id + '/task/' + task.id)"
                                       class="btn btn-sm"> Подробнее </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div v-else>
                    <div class="alert alert-info shadow-lg basis-1/2 mt-10">
                        <div>
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                 class="stroke-current flex-shrink-0 w-6 h-6">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                            </svg>
                            <span>
                        Задач тут нет.

                        </span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="text-center">
                <a :href="'#modal-' + column.id" class="btn btn-circle btn-outline mt-10 mb-10">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                         stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1"
                              d="M11 11v-11h1v11h11v1h-11v11h-1v-11h-11v-1h11z"/>
                    </svg>
                </a>
            </div>

            <div class="modal" :id="'modal-' + column.id">
                <div class="modal-box">
                    <h3 class="font-bold text-lg">Создание задачи</h3>
                    <p class="py-4">
                        Заведите новую задачу, чтобы стать на один шаг ближе к мечте
                    </p>
                    <div class="flex flex-col items-center">
                        <success-alert
                                :is-success="isSuccessTask"
                        >Вы успешно создали задачу!
                        </success-alert>
                        <error-alert
                                :is-error="isErrorTask"
                        > {{ errorDescriptionTask }}
                        </error-alert>
                    </div>
                    <create-task-form @create="createTask" :board-column-id="column.id"/>
                    <div class="modal-action">
                        <a href="#" class="btn">Закрыть</a>
                    </div>
                </div>
            </div>
        </div>
        <div>
            <a class="btn btn-outline btn-success gap-3" href="#create_column">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                     stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1"
                          d="M11 11v-11h1v11h11v1h-11v11h-1v-11h-11v-1h11z"/>
                </svg>
                Добавить колонку
            </a>

            <div class="modal" id="create_column">
                <div class="modal-box">
                    <h3 class="font-bold text-lg">Создание новой колонки</h3>
                    <p class="py-4">
                        Добавьте новую колонку
                    </p>
                    <div class="flex flex-col items-center">
                        <success-alert
                                :is-success="isSuccessColumn"
                        >Вы успешно создали колонку!
                        </success-alert>
                        <error-alert
                                :is-error="isErrorColumn"
                        > {{ errorDescriptionColumn }}
                        </error-alert>
                    </div>
                    <create-task-column-form @create="createColumn" :board-id="$route.params.board_id"/>
                    <div class="modal-action">
                        <a href="#" class="btn">Закрыть</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<script>
import EachOrganizationHeader from "@/components/ui/EachOrganizationHeader.vue";
import CreateTaskForm from "@/components/CreateTaskForm.vue";
import axios from "axios";
import store from "@/store";
import CreateTaskColumnForm from "@/components/CreateTaskColumnForm.vue";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import Constants from "@/components/constants";

export default {
    components: {SuccessAlert, ErrorAlert, CreateTaskColumnForm, CreateTaskForm, EachOrganizationHeader},
    data() {
        return {
            isSuccessTask: false,
            isErrorTask: false,
            errorDescriptionTask: '',
            isSuccessColumn: false,
            isErrorColumn: false,
            errorDescriptionColumn: ''
        }
    },
    props: {
        organization: {
            type: Object,
            required: true
        },
        board: {
            type: Object,
            required: true
        },
        isCurrentUserOrganizationAdmin: {
            type: Boolean,
            required: true
        }
    },
    methods: {
        async createTask(task) {
            await axios.post("http://localhost:8080/api/tracker/v1/task/create", {
                name: task.name,
                description: task.description,
                boardColumnId: task.boardColumnId
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.isSuccessTask = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                this.isErrorTask = true;
                this.errorDescriptionTask = Constants.ERROR_SERVER_DESCRIPTION;
                console.log(response);
            })
        },
        async createColumn(column) {
            await axios.post("http://localhost:8080/api/tracker/v1/boardColumn/create", {
                name: column.name,
                boardId: column.boardId
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.isSuccessColumn = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                this.isErrorColumn = true;
                this.errorDescriptionColumn = Constants.ERROR_SERVER_DESCRIPTION;
                console.log(response)
            })
        }
    }
}
</script>