<template>
    <div
            v-if="this.isCurrentUserOrganizationAdmin"
    >
        <div class="grid lg:grid-cols-3 mt-10 gap-4 justify-items-center m-10">
            <div>
                <div class="card bg-base-200 shadow-xl">
                    <div class="card-body">
                        <div class="badge badge-primary font-bold">Название компании</div>
                        <h2 class="card-title text-3xl">
                            {{ this.organization.name }}
                        </h2>
                        <p>
                            Это страница со всеми сотрудниками компании. Вы можете найти их по любым параметрам.
                            На этой странице отображаются все сотрудники - и действующие, и уволенные.
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <div class="flex justify-center text-sm breadcrumbs mt-10">
            <ul>
                <li><a @click="$router.push('/organizations/' + organization.id)">Компания</a></li>
                <li>Все сотрудники</li>
            </ul>
        </div>

        <div class="flex justify-center m-10" id="org_workers">
            <div class="card w-full bg-base-300 shadow-xl">
                <div class="card-body">
                    <div class="overflow-x-auto w-full">
                        <h2 class="mb-5 mt-5 text-2xl font-bold text-base-color text-center"> Сотрудники организации </h2>
                        <table class="table w-full">
                            <!-- head -->
                            <thead>
                            <tr>
                                <th>Имя</th>
                                <th>Должность</th>
                                <th>Права администратора</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>

                            <tr
                                    v-for="worker in organization.workers"
                                    :key="worker.id"
                            >
                                <td>
                                    <div class="flex items-center space-x-3">
                                        <div>
                                            <div class="font-bold">
                                                {{ worker.firstName + " " + worker.lastName }}
                                                <span v-if="worker.userId === store.state.user.id" class="badge badge-sm ml-2"> Это Вы</span>
                                                <span v-if="worker.status !== 'WORK'" class="badge badge-sm ml-2 badge-error">Уволен</span>
                                            </div>
                                            <div class="text-sm opacity-50">{{ worker.email }}</div>
                                        </div>
                                    </div>
                                </td>

                                <td v-if="worker.status === 'WORK'">
                                    <div v-if="worker.workerFunctions.length === 0">
                                        <button
                                                class="btn btn-sm btn-outline btn-accent"
                                                @click="$router.push('/organizations/' + this.$route.params.id + '/workers/get/' + worker.id)"
                                        >Добавить должность
                                        </button>
                                    </div>

                                    <div v-else>
                                        <div
                                                v-for="func in worker.workerFunctions"
                                                :key="func.id"
                                        >
                                            <span class="badge badge-ghost">{{ func.functionName }}</span>
                                        </div>
                                    </div>
                                </td>
                                <td v-else>
                                    <span class="badge badge-error font-bold">Сотрудник уволен</span>
                                </td>

                                <td v-if="worker.isOrganizationAdmin === true && worker.userId === store.state.user.id && worker.status === 'WORK'">
                                    <div class="tooltip" data-tip="Вы не можете удалить себя">
                                        <button class="btn btn-circle btn-outline btn-sm btn-info">
                                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                 stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                                <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 12.75l6 6 9-13.5"/>
                                            </svg>
                                        </button>
                                    </div>
                                </td>

                                <td v-else-if="worker.isOrganizationAdmin === true && worker.userId === organization.ownerUserId && worker.status === 'WORK'">
                                    <div class="tooltip" data-tip="Нельзя удалить владельца">
                                        <button class="btn btn-circle btn-outline btn-sm btn-info">
                                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                 stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                                <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 12.75l6 6 9-13.5"/>
                                            </svg>
                                        </button>
                                    </div>
                                </td>

                                <td v-else-if="worker.isOrganizationAdmin === true && worker.userId !== store.state.user.id && organization.ownerUserId !== store.state.user.id && worker.status === 'WORK'">
                                    <button class="btn gap-2 btn-sm btn-error btn-disabled"
                                            @click="deleteAdminFromOrganization(worker.id)">
                                        Удалить права
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                             stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                            <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
                                        </svg>
                                    </button>
                                </td>

                                <td v-else-if="worker.isOrganizationAdmin === true && worker.userId !== store.state.user.id && organization.ownerUserId === store.state.user.id && worker.status === 'WORK'">
                                    <button class="btn gap-2 btn-outline btn-sm btn-error"
                                            @click="deleteAdminFromOrganization(worker.id)">
                                        Удалить права
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                             stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                            <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
                                        </svg>
                                    </button>
                                </td>

                                <td v-else-if="worker.status !== 'WORK'">
                                    <span class="badge badge-error font-bold">Сотрудник уволен</span>
                                </td>

                                <td v-else>
                                    <button class="btn gap-2 btn-outline btn-sm btn-success"
                                            @click="addAdminForOrganization(worker.id)">
                                        Добавить права
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                             stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
                                        </svg>
                                    </button>
                                </td>

                                <th>
                                    <button
                                            class="btn btn-ghost btn-xs"
                                            @click="$router.push('/organizations/' + this.$route.params.id + '/workers/get/' + worker.id)"
                                    >Подробнее
                                    </button>
                                </th>
                            </tr>
                            </tbody>
                            <!-- foot -->
                            <tfoot>
                            <tr>
                                <th>Имя</th>
                                <th>Должность</th>
                                <th>Права администратора</th>
                                <th></th>
                            </tr>
                            </tfoot>

                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div v-else>
        <div class="alert alert-error shadow-lg">
            <div>
                <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                <span>У вас нет доступа к этой странице!</span>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import store from "@/store";

export default {
    computed: {
        store() {
            return store
        }
    },
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
            isCurrentUserOrganizationAdmin: false
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

            console.log("ADMIN? : " + this.isCurrentUserOrganizationAdmin)
        },
        async addAdminForOrganization(workerId) {
            await axios.put("http://localhost:8080/api/tracker/v1/organization/" + this.organization.id + "/add/admin/" + workerId,
                null, {
                    headers: {
                        Authorization: store.state.token
                    }
                }
            ).then((response) => {
                console.log(response.data.payload)
                this.$router.go(this.$router.currentRoute)
            }).catch((response) => {
                console.log(response)
            })
        },
        async deleteAdminFromOrganization(workerId) {
            await axios.put("http://localhost:8080/api/tracker/v1/organization/" + this.organization.id + "/delete/admin/" + workerId,
                null, {
                    headers: {
                        Authorization: store.state.token
                    }
                }
            ).then((response) => {
                console.log(response.data.payload)
                this.$router.go(this.$router.currentRoute)
            }).catch((response) => {
                console.log(response)
            })
        },
    },
    mounted() {
        this.getOrganization()
    }
}
</script>