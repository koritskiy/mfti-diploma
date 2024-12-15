<template>
    <div class="flex justify-center text-sm breadcrumbs mt-10">
        <ul>
            <li><a @click="$router.push('/organizations/' + organization.id)">Компания</a></li>
            <li><a @click="$router.push('/organizations/' + organization.id + '/boards/get/' + board.id)">{{
                board.name
                }}</a></li>
            <li>{{ task.name }}</li>
        </ul>
    </div>
    <div class="flex flex-row justify-center mt-10 mb-10">
        <div class="card bg-base-100 shadow-xl basis-1/2">
            <div class="card-body grid grid-cols-2 gap-4">
                <div>
                    <span class="badge badge-success font-bold"> Название задачи </span>
                    <h2 class="card-title mt-2">{{ task.name }}</h2>

                    <span class="badge badge-info font-bold mt-5"> Описание задачи </span>
                    <p class="mt-2">
                        {{ task.description }}
                    </p>

                    <div v-if="task.branches.length > 0">
                        <span class="badge badge-warning font-bold mt-5"> Привязанные ветки </span>
                        <div class="mt-2 flex flex-row">
                            <div
                                    v-for="branch in task.branches"
                                    :key="branch.id"
                            >
                                <a
                                        class="btn btn-sm gap-2 mr-2"
                                        :href="'#each-branch-' + branch.id"
                                        @click="compareBranches(branch)"
                                >
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                         stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                        <path stroke-linecap="round" stroke-linejoin="round"
                                              d="M17.25 6.75L22.5 12l-5.25 5.25m-10.5 0L1.5 12l5.25-5.25m7.5-3l-4.5 16.5"/>
                                    </svg>
                                    {{ branch.branchName }}
                                </a>

                                <div class="modal" :id="'each-branch-' + branch.id">
                                    <div class="modal-box">
                                        <h3 class="font-bold text-lg">
                                            Ветка: {{ branch.branchName }}
                                        </h3>
                                        <p class="py-4">
                                            Ниже рассмотрено сравнение двух веток - новой и базовой веток.
                                        </p>

                                        <div v-if="this.pull_request.state !== '' && this.pull_request.state === 'open'"
                                             class="alert alert-success shadow-lg mb-3">
                                            <div>
                                                <svg xmlns="http://www.w3.org/2000/svg"
                                                     class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                                                     viewBox="0 0 24 24">
                                                    <path stroke-linecap="round" stroke-linejoin="round"
                                                          stroke-width="2"
                                                          d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                                </svg>
                                                <span>
                                                    <span class="underline">
                                                        <a
                                                                :href="this.pull_request.html_url"
                                                                target="_blank"
                                                        >
                                                            Открытый Pull Request для этой ветки:
                                                        </a>
                                                    </span>
                                                    <span class="font-bold">{{ this.pull_request.title }}</span>
                                                </span>
                                            </div>
                                        </div>

                                        <div v-if="this.pull_request.state !== '' && this.pull_request.state === 'closed'"
                                             class="alert alert-success shadow-lg mb-3">
                                            <div>
                                                <svg xmlns="http://www.w3.org/2000/svg"
                                                     class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                                                     viewBox="0 0 24 24">
                                                    <path stroke-linecap="round" stroke-linejoin="round"
                                                          stroke-width="2"
                                                          d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                                </svg>
                                                <span>
                                                    <span class="underline">
                                                        <a
                                                                :href="this.pull_request.html_url"
                                                                target="_blank"
                                                        >
                                                            Закрытый Pull Request для этой ветки:
                                                        </a>
                                                    </span>
                                                    <span class="font-bold">{{ this.pull_request.title }}</span>
                                                </span>
                                            </div>
                                        </div>

                                        <ul
                                                class="menu bg-base-200 w-full rounded-box"
                                                v-if="comparing.total_commits > 0"
                                        >
                                            <li
                                                    v-for="(commit, objKey) in comparing.commits"
                                                    :key="commit.id"
                                            >
                                                <a :href="commit.html_url" target="_blank">
                                                    <span class="badge badge-success">{{ objKey + 1 }}</span>
                                                    {{ commit.commit.author.name }} -> {{ commit.commit.message }}
                                                </a>
                                            </li>

                                        </ul>

                                        <div class="alert alert-info shadow-lg"
                                             v-else-if="comparing.total_commits === 0 && pull_request.state === ''">
                                            <div>
                                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                     class="stroke-current flex-shrink-0 w-6 h-6">
                                                    <path stroke-linecap="round" stroke-linejoin="round"
                                                          stroke-width="2"
                                                          d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                                                </svg>
                                                <span>Новых коммитов в этой ветке нет.</span>
                                            </div>
                                        </div>
                                        <div class="modal-action">
                                            <a href="#" class="btn">Закрыть</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="divider"></div>
                    <div class="card bg-base-200 shadow-xl">
                        <div class="card-body">
                            <span class="font-bold"> Интеграция с GitHub </span>
                            <div v-if="this.$store.state.gitHubToken === ''">
                                <div class="alert alert-warning shadow-lg">
                                    <div>
                                        <svg xmlns="http://www.w3.org/2000/svg"
                                             class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                                             viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                  d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                                        </svg>
                                        <span>
                                            У Вас не добавлен токен авторизации для GitHub. Сделайте это в настройках.
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div v-else>
                                <create-branch-for-repo-form :github="this.github" @create="createNewBranch"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="w-full">
                    <ul class="menu bg-base-200 p-2 rounded-box opacity-1">
                        <li class="font-bold">
                            <a href="#tags">
                                <div class="flex gap-2 items-center">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                         stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v12m6-6H6"/>
                                    </svg>
                                    Теги задач
                                </div>
                            </a>
                        </li>
                        <div class="modal" id="tags">
                            <div class="modal-box">
                                <h3 class="font-bold text-lg">Добавьте теги к задаче</h3>
                                <div v-for="tag in task.taskTagList" :key="tag.id" class="badge badge-success mr-2">
                                    {{ tag.tagValue }}
                                </div>
                                <div v-if="organization.tags.length > 0">
                                    <div class="flex flex-col items-center">
                                        <success-alert
                                                :is-success="isSuccessTag"
                                        >Вы успешно привязали тег!
                                        </success-alert>
                                        <error-alert
                                                class="w-1/2"
                                                :is-error="isErrorTag"
                                        > {{ errorDescriptionTag }}
                                        </error-alert>
                                    </div>
                                    <add-tag-form @create="linkTagForTask" :organization="organization" :task="task"/>
                                </div>
                                <div v-else>
                                    <div class="alert alert-warning shadow-lg mt-5 mb-5">
                                        <div>
                                            <svg xmlns="http://www.w3.org/2000/svg"
                                                 class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                                                 viewBox="0 0 24 24">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                      d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                                            </svg>
                                            <span>Внимание: в организации нет ни одного тега. Давайте добавим!</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-action">
                                    <a href="#" class="btn">Закрыть</a>
                                </div>
                            </div>
                        </div>
                        <div v-if="task.taskTagList.length > 0">
                            <li v-for="tag in task.taskTagList" :key="tag.id">
                                <a @click="unlinkTag(tag.id)">
                                    {{ tag.tagValue }}
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                         stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                        <path stroke-linecap="round" stroke-linejoin="round"
                                              d="M12 9.75L14.25 12m0 0l2.25 2.25M14.25 12l2.25-2.25M14.25 12L12 14.25m-2.58 4.92l-6.375-6.375a1.125 1.125 0 010-1.59L9.42 4.83c.211-.211.498-.33.796-.33H19.5a2.25 2.25 0 012.25 2.25v10.5a2.25 2.25 0 01-2.25 2.25h-9.284c-.298 0-.585-.119-.796-.33z"/>
                                    </svg>
                                </a>
                            </li>
                        </div>
                        <div v-else class="">
                            <li>
                                <a><span class="badge badge-error">Тегов пока нет</span></a>
                            </li>
                        </div>

                        <li class="font-bold">
                            <a href="#status">
                                <div class="flex gap-2 items-center">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                         stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                        <path stroke-linecap="round" stroke-linejoin="round"
                                              d="M2.25 7.125C2.25 6.504 2.754 6 3.375 6h6c.621 0 1.125.504 1.125 1.125v3.75c0 .621-.504 1.125-1.125 1.125h-6a1.125 1.125 0 01-1.125-1.125v-3.75zM14.25 8.625c0-.621.504-1.125 1.125-1.125h5.25c.621 0 1.125.504 1.125 1.125v8.25c0 .621-.504 1.125-1.125 1.125h-5.25a1.125 1.125 0 01-1.125-1.125v-8.25zM3.75 16.125c0-.621.504-1.125 1.125-1.125h5.25c.621 0 1.125.504 1.125 1.125v2.25c0 .621-.504 1.125-1.125 1.125h-5.25a1.125 1.125 0 01-1.125-1.125v-2.25z"/>
                                    </svg>
                                    Статус
                                </div>
                            </a>
                        </li>

                        <li>
                            <a><span class="badge badge-info">{{ task.status }}</span></a>
                        </li>

                        <div class="modal" id="status">
                            <div class="modal-box">
                                <div class="flex flex-col items-center">
                                    <success-alert
                                            :is-success="isSuccessStatus"
                                    >Вы успешно поменяли статус!
                                    </success-alert>
                                    <error-alert
                                            class="w-1/2"
                                            :is-error="isErrorStatus"
                                    > {{ errorDescriptionStatus }}
                                    </error-alert>
                                </div>
                                <change-task-status-form :task-board="this.board" @create="changeTaskStatus"/>
                                <div class="modal-action">
                                    <a href="#" class="btn">Закрыть</a>
                                </div>
                            </div>
                        </div>

                        <li class="font-bold">
                            <a href="#assignee">
                                <div class="flex gap-2 items-center">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                         stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                                        <path stroke-linecap="round" stroke-linejoin="round"
                                              d="M19 7.5v3m0 0v3m0-3h3m-3 0h-3m-2.25-4.125a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zM4 19.235v-.11a6.375 6.375 0 0112.75 0v.109A12.318 12.318 0 0110.374 21c-2.331 0-4.512-.645-6.374-1.766z"/>
                                    </svg>
                                    Исполнитель
                                </div>
                            </a>
                        </li>
                        <div class="modal" id="assignee">
                            <div class="modal-box">
                                <h3 class="font-bold text-lg">Добавьте исполнителя к задаче</h3>
                                <div v-if="this.organization.workers.length > 0">
                                    <div class="flex flex-col items-center">
                                        <success-alert
                                                :is-success="isSuccessWorker"
                                        >Вы успешно привязали задачу к сотруднику!
                                        </success-alert>
                                        <error-alert
                                                class="w-1/2"
                                                :is-error="isErrorWorker"
                                        > {{ errorDescriptionWorker }}
                                        </error-alert>
                                    </div>
                                    <add-assignee-for-task-form @create="linkTaskForWorker" :organization="organization"
                                                                :task="task"/>
                                </div>
                                <div v-else>
                                    <div class="alert alert-warning shadow-lg mt-5 mb-5">
                                        <div>
                                            <svg xmlns="http://www.w3.org/2000/svg"
                                                 class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                                                 viewBox="0 0 24 24">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                      d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                                            </svg>
                                            <span>Внимание: в организации нет ни одного сотрудника. Добавьте сотрудников и попробуйте еще раз.</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-action">
                                    <a href="#" class="btn">Закрыть</a>
                                </div>
                            </div>
                        </div>

                        <div v-if="this.task.worker !== null">
                            <li>
                                <a @click="removeAssignFromTask(this.task.id)">
                                    <span class="badge badge-success gap-2">
                                          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                               class="inline-block w-4 h-4 stroke-current">
                                              <path stroke-linecap="round"
                                                    stroke-linejoin="round"
                                                    stroke-width="2"
                                                    d="M6 18L18 6M6 6l12 12"></path>
                                          </svg>
                                        {{ this.task.worker.lastName }} {{ this.task.worker.firstName }}
                                    </span>
                                </a>
                            </li>
                        </div>
                        <div v-else>
                            <li>
                                <a>Исполнитель не назначен</a>
                            </li>
                        </div>

                        <li class="font-bold">
                            <a @click="deleteTask">
                                <div class="flex gap-2 items-center">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                         stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                                        <path stroke-linecap="round" stroke-linejoin="round"
                                              d="M12 9.75L14.25 12m0 0l2.25 2.25M14.25 12l2.25-2.25M14.25 12L12 14.25m-2.58 4.92l-6.375-6.375a1.125 1.125 0 010-1.59L9.42 4.83c.211-.211.498-.33.796-.33H19.5a2.25 2.25 0 012.25 2.25v10.5a2.25 2.25 0 01-2.25 2.25h-9.284c-.298 0-.585-.119-.796-.33z"/>
                                    </svg>
                                    Удалить задачу
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import store from "@/store";
import AddTagForm from "@/components/AddTagForTaskForm.vue";
import AddAssigneeForTaskForm from "@/components/AddAssigneeForTaskForm.vue";
import ChangeTaskStatusForm from "@/components/ChangeTaskStatusForm.vue";
import CreateBranchForRepoForm from "@/components/CreateBranchForRepoForm.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import Constants from "@/components/constants";

export default {
    components: {
        ErrorAlert,
        SuccessAlert, CreateBranchForRepoForm, ChangeTaskStatusForm, AddAssigneeForTaskForm, AddTagForm
    },
    data() {
        return {
            task: {
                id: 0,
                name: '',
                description: '',
                taskTagList: [
                    {id: 0, tagValue: ''}

                ],
                worker: '',
                status: '',
                branches: []
            },
            organization: {
                id: 0,
                name: '',
                address: '',
                link: '',
                workers: [],
                functions: [],
                taskBoards: [],
                tags: []
            },
            board: {
                id: 0,
                name: '',
                boardColumns: []
            },
            github: {
                repositories: []
            },
            comparing: {
                status: '',
                ahead_by: 0,
                behind_by: 0,
                total_commits: 0,
                commits: []
            },
            pull_request: {
                state: '',
                title: '',
                html_url: ''
            },
            isSuccessTag: false,
            isErrorTag: false,
            errorDescriptionTag: '',
            isSuccessStatus: false,
            isErrorStatus: false,
            errorDescriptionStatus: '',
            isSuccessWorker: false,
            isErrorWorker: false,
            errorDescriptionWorker: ''
        }
    },
    methods: {
        async getTask() {
            await axios.get("http://localhost:8080/api/tracker/v1/task/get/" + this.$route.params.task_id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.task = response.data.payload;
                console.log(response.data.payload)
            }).catch((response) => {
                console.log(response)
            })
        },
        async getOrganization() {
            await axios.get("http://localhost:8080/api/tracker/v1/organization/getById/" + this.$route.params.organization_id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.organization = response.data.payload;
                console.log(response.data.payload)
            }).catch((error) => {
                console.log(error)
            })
        },
        async linkTaskForWorker(worker) {
            await axios.put("http://localhost:8080/api/tracker/v1/task/assign", {
                workerId: worker.selectId,
                taskId: this.task.id
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.isSuccessWorker = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                console.log(response)
                this.isErrorWorker = true;
                this.errorDescriptionWorker = Constants.ERROR_SERVER_DESCRIPTION;
            })
        },
        async linkTagForTask(tag) {
            await axios.put("http://localhost:8080/api/tracker/v1/tag/link/tagToTask", {
                taskId: this.task.id,
                tagId: tag.selectId
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.isSuccessTag = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                console.log(response)
                this.isErrorTag = false;
                this.errorDescriptionTag = Constants.ERROR_SERVER_DESCRIPTION;
            })
        },
        async deleteTask() {
            await axios.delete("http://localhost:8080/api/tracker/v1/task/delete/" + this.task.id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.$router.push({
                    name: 'get-board',
                    params: {organizationId: this.$route.params.organization_id, boardId: this.$route.params.board_id}
                })
            }).catch((response) => {
                console.log(response)
            })
        },
        async getBoard() {
            await axios.get("http://localhost:8080/api/tracker/v1/taskBoard/get/" + this.$route.params.board_id, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.board = response.data.payload;
                console.log(this.board)
            })
        },
        async changeTaskStatus(status) {
            console.log(status)
            await axios.put("http://localhost:8080/api/tracker/v1/task/updateStatus/" + status.selectId + "/" + this.task.id, null, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                this.task = response.data.payload;
                console.log(response.data.payload)
                this.isSuccessStatus = true;
            }).catch((response) => {
                console.log(response);
                this.isErrorStatus = true;
                this.errorDescriptionStatus = Constants.ERROR_SERVER_DESCRIPTION;
            })
        },
        async getGitHubRepositories() {
            await axios.get("https://api.github.com/user/repos?sort=updated", {
                headers: {
                    'Accept': 'application/vnd.github+json',
                    'Authorization': 'Bearer ' + store.state.gitHubToken,
                    'X-GitHub-Api-Version': '2022-11-28'
                }
            }).then((response) => {
                this.github.repositories = response.data;
            }).catch((response) => {
                console.log(response);
            })
        },
        async createNewBranch(newBranch) {
            await axios.post("https://api.github.com/repos/" + newBranch.repositoryFullName + "/git/refs", {
                ref: "refs/heads/" + newBranch.newBranchName,
                sha: newBranch.fromBranchSha
            }, {
                headers: {
                    'Accept': 'application/vnd.github+json',
                    'Authorization': 'Bearer ' + store.state.gitHubToken,
                    'X-GitHub-Api-Version': '2022-11-28'
                }
            }).then((response) => {
                this.saveNewBranch(newBranch, response)
                console.log(response)
            }).catch((response) => {
                console.log(response);
            })
        },
        async saveNewBranch(newBranch, responseFromGitHub) {
            console.log(newBranch)
            console.log(responseFromGitHub)
            await axios.post("http://localhost:8080/api/tracker/v1/git/branch/create", {
                repoFullName: newBranch.repositoryFullName,
                branchName: newBranch.newBranchName,
                ref: responseFromGitHub.data.ref,
                taskId: this.task.id
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.$router.go(this.$router.currentRoute)
            }).catch((response) => {
                console.log(response)
            })
        },
        async compareBranches(branch) {
            await axios.get("https://api.github.com/repos/" + branch.repoFullName + "/compare/main..." + branch.branchName, {
                headers: {
                    'Accept': 'application/vnd.github+json',
                    'Authorization': 'Bearer ' + store.state.gitHubToken,
                    'X-GitHub-Api-Version': '2022-11-28'
                }
            }).then((response) => {
                this.comparing = response.data;
                this.getPullRequest(branch);
            }).catch((response) => {
                console.log(response)
            })
        },
        async getPullRequest(branch) {
            await axios.get("https://api.github.com/repos/" + branch.repoFullName + "/pulls?state=all&head=" + branch.repoFullName + ":" + branch.branchName, {
                headers: {
                    'Accept': 'application/vnd.github+json',
                    'Authorization': 'Bearer ' + store.state.gitHubToken,
                    'X-GitHub-Api-Version': '2022-11-28'
                }
            }).then((response) => {
                if (response.data[0] !== undefined) {
                    this.pull_request = response.data[0];
                } else {
                    this.pull_request.state = ''
                    this.pull_request.title = ''
                    this.pull_request.html_url = ''
                }
            }).catch((response) => {
                console.log(response)
            })
        },
        async unlinkTag(taskTagId) {
            await axios.delete("http://localhost:8080/api/tracker/v1/tag/unlink/tag/" + taskTagId, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload);
                this.$router.go(this.$router.currentRoute);
            }).catch((response) => {
                console.log(response);
            })
        },
        async removeAssignFromTask(taskId) {
            await axios.put("http://localhost:8080/api/tracker/v1/task/remove/assign/" + taskId, null, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.$router.go(this.$router.currentRoute);
            }).catch((response) => {
                console.log(response)
            })
        }
    },
    mounted() {
        this.$store.commit('setPageName', 'each-task-page');
        this.getTask();
        this.getOrganization()
        this.getBoard();
        this.getGitHubRepositories();
    }
}
</script>