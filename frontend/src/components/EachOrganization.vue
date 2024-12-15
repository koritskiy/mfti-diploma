<template>
    <div
            v-if="this.isCurrentUserOrganizationAdmin"
            class="grid lg:grid-cols-3 mt-10 gap-4 justify-items-center m-10"
    >
        <div>
            <div class="card bg-base-200 shadow-xl">
                <div class="card-body">
                    <div class="badge badge-primary font-bold">Название компании</div>
                    <h2 class="card-title text-3xl">
                        {{ organization.name }}
                    </h2>
                    <p>
                        Это страница Вашей компании.
                        <span v-if="this.isCurrentUserOrganizationAdmin">
                            Здесь Вы можете управлять всеми сотрудниками компании, привязывать к ним должности
                            и управлять досками задач.
                        </span>
                        <span v-else>
                            Здесь Вы можете посмотреть все свои задачи, создать новые и управлять своим рабочим временем.
                        </span>
                    </p>
                    <div v-if="this.isCurrentUserOrganizationAdmin" class="card-actions justify-center mt-5">
                        <a @click="$router.push({path: '/organizations/' + this.organization.id + '/workers/create'})"
                           class="btn btn-outline btn-info btn-sm">
                            Добавить сотрудника
                        </a>
                    </div>
                </div>
            </div>
        </div>
        <div>
            <ul class="menu bg-base-200 p-2 rounded-box">
                <li>
                    <a href="#manage-task-boards">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2"
                             stroke="currentColor" class="w-5 h-5">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                  d="M9 12h3.75M9 15h3.75M9 18h3.75m3 .75H18a2.25 2.25 0 002.25-2.25V6.108c0-1.135-.845-2.098-1.976-2.192a48.424 48.424 0 00-1.123-.08m-5.801 0c-.065.21-.1.433-.1.664 0 .414.336.75.75.75h4.5a.75.75 0 00.75-.75 2.25 2.25 0 00-.1-.664m-5.8 0A2.251 2.251 0 0113.5 2.25H15c1.012 0 1.867.668 2.15 1.586m-5.8 0c-.376.023-.75.05-1.124.08C9.095 4.01 8.25 4.973 8.25 6.108V8.25m0 0H4.875c-.621 0-1.125.504-1.125 1.125v11.25c0 .621.504 1.125 1.125 1.125h9.75c.621 0 1.125-.504 1.125-1.125V9.375c0-.621-.504-1.125-1.125-1.125H8.25zM6.75 12h.008v.008H6.75V12zm0 3h.008v.008H6.75V15zm0 3h.008v.008H6.75V18z"/>
                        </svg>

                        Управление досками задач
                    </a>
                </li>
                <li>
                    <a href="#org_workers">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2"
                             stroke="currentColor" class="w-5 h-5">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                  d="M18 18.72a9.094 9.094 0 003.741-.479 3 3 0 00-4.682-2.72m.94 3.198l.001.031c0 .225-.012.447-.037.666A11.944 11.944 0 0112 21c-2.17 0-4.207-.576-5.963-1.584A6.062 6.062 0 016 18.719m12 0a5.971 5.971 0 00-.941-3.197m0 0A5.995 5.995 0 0012 12.75a5.995 5.995 0 00-5.058 2.772m0 0a3 3 0 00-4.681 2.72 8.986 8.986 0 003.74.477m.94-3.197a5.971 5.971 0 00-.94 3.197M15 6.75a3 3 0 11-6 0 3 3 0 016 0zm6 3a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0zm-13.5 0a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0z"/>
                        </svg>

                        Управление сотрудниками
                    </a>
                </li>
                <li>
                    <a href="#org_fin_data">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2"
                             stroke="currentColor" class="w-5 h-5">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                  d="M2.25 18.75a60.07 60.07 0 0115.797 2.101c.727.198 1.453-.342 1.453-1.096V18.75M3.75 4.5v.75A.75.75 0 013 6h-.75m0 0v-.375c0-.621.504-1.125 1.125-1.125H20.25M2.25 6v9m18-10.5v.75c0 .414.336.75.75.75h.75m-1.5-1.5h.375c.621 0 1.125.504 1.125 1.125v9.75c0 .621-.504 1.125-1.125 1.125h-.375m1.5-1.5H21a.75.75 0 00-.75.75v.75m0 0H3.75m0 0h-.375a1.125 1.125 0 01-1.125-1.125V15m1.5 1.5v-.75A.75.75 0 003 15h-.75M15 10.5a3 3 0 11-6 0 3 3 0 016 0zm3 0h.008v.008H18V10.5zm-12 0h.008v.008H6V10.5z"/>
                        </svg>
                        Финансовые данные организации
                    </a>
                </li>
                <li>
                    <a href="#org_stats">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                             stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/>
                        </svg>
                        Статистика организации
                    </a>
                </li>
            </ul>
        </div>
        <div>
            <div class="card bg-base-200 shadow-xl">
                <div class="card-body">
                    <div class="badge badge-info font-bold">Новый функционал</div>
                    <h2 class="card-title">
                        Сервис найма сотрудников
                    </h2>
                    <p>
                        Этот сервис поможет наладить HR-процессы Вашей компании. С помощью него Вы сможете
                        заводить профиль кандидатов, сохранять задачи для будущих собеседований
                        и переводить кандидатов в сотрудников.
                    </p>
                    <div class="card-actions justify-center mt-5">
                        <a @click="$router.push({path: '/organizations/' + this.organization.id + '/hiring', params: {organization: this.organization}})"
                           class="btn btn-primary btn-sm">
                            Перейти в сервис найма
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="flex justify-center" v-else>
        <each-organization-header
                :organization="organization"
                :is-current-user-organization-admin="isCurrentUserOrganizationAdmin"
        />
    </div>

    <div class="mt-10" id="manage-task-boards">
        <h2 class="mb-5 mt-5 text-2xl font-bold text-base-color text-center"> Все доски задач организации </h2>
        <div class="flex flex-col items-center">
            <success-alert
                    class="w-1/2"
                    :is-success="isSuccessTaskBoard"
            >Вы успешно создали доску!
            </success-alert>
            <error-alert
                    class="w-1/2"
                    :is-error="isErrorTaskBoard"
            > {{ errorDescriptionTaskBoard }}
            </error-alert>
        </div>
        <div
                class="flex justify-center items-center gap-20"
        >
            <div v-if="this.organization.taskBoards.length > 0">
                <div
                        class="flex gap-10"
                        :class="[this.isCurrentUserOrganizationAdmin ? 'flex-col' : 'flex-row']"
                >
                    <div v-for="tb in organization.taskBoards" :key="tb.id">
                        <div class="card bg-base-200 shadow-xl">
                            <div class="card-body">
                                <h2 class="card-title">{{ tb.name }}</h2>
                                <p>Самая полезная доска</p>
                                <div class="card-actions justify-start">
                                    <button
                                            @click="$router.push('/organizations/' + this.$route.params.id + '/boards/get/' + tb.id)"
                                            class="btn btn-primary btn-sm"
                                    >
                                        Перейти
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div v-else>
                <div class="alert alert-info shadow-lg basis-1/2">
                    <div>
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                             class="stroke-current flex-shrink-0 w-6 h-6">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                        </svg>
                        <span>
                        Досок задач пока нет.

                        </span>
                    </div>
                </div>
            </div>

            <div class="basis-1/2"
                 v-if="this.isCurrentUserOrganizationAdmin">
                <create-task-board-form @create="createBoard"/>
            </div>
        </div>
    </div>

    <div v-if="this.isCurrentUserOrganizationAdmin">
        <div class="flex justify-center m-10">
            <div class="overflow-x-auto w-1/2 p-5">
                <h2 class="mb-5 mt-5 text-2xl font-bold text-base-color text-center"> Теги организации </h2>
                <table class="table w-full">
                    <!-- head -->
                    <thead>
                    <tr>
                        <th>Название тега</th>
                        <th>Описание тега</th>
                        <th>Удалить тег</th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr
                            v-for="tag in organization.tags"
                            :key="tag.id"
                    >
                        <td>
                            <div class="flex items-center space-x-3">
                                <div>
                                    <div class="font-bold">{{ tag.name }}</div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <span class="badge badge-ghost badge-sm">Описание тега</span>
                        </td>
                        <td>
                            <a class="btn gap-2 btn-xs btn-error btn-outline"
                               @click="deleteTag(tag.id)">
                                Удалить тег
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                     stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
                                </svg>
                            </a>
                        </td>
                    </tr>
                    </tbody>
                    <!-- foot -->
                    <tfoot>
                    <tr>
                        <th>Название тега</th>
                        <th>Описание тега</th>
                        <th>Удалить тег</th>
                    </tr>
                    </tfoot>

                </table>
            </div>
            <div class="overflow-x-auto w-1/2 p-5">
                <div class="flex flex-col items-center">
                    <success-alert
                            :is-success="isSuccessTag"
                    >Вы успешно создали тег!
                    </success-alert>
                    <success-alert
                            :is-success="isSuccessTagDelete"
                    >Вы успешно удалили тег!
                    </success-alert>
                    <error-alert
                            :is-error="isErrorTag"
                    > {{ errorDescriptionTag }}
                    </error-alert>
                </div>
                <create-tag-form @create="createTag"/>
            </div>
        </div>

        <div class="flex justify-center m-10" id="org_workers">
            <div class="card w-full bg-base-300 shadow-xl">
                <div class="card-body">
                    <div class="overflow-x-auto w-full">
                        <h2 class="mb-5 mt-5 text-2xl font-bold text-base-color text-center"> Сотрудники
                            организации </h2>
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
                                    v-show="worker.status === 'WORK'"
                            >
                                <td>
                                    <div class="flex items-center space-x-3">
                                        <div>
                                            <div class="font-bold">
                                                {{ worker.firstName + " " + worker.lastName }}
                                                <span v-if="worker.userId === store.state.user.id"
                                                      class="badge badge-sm ml-2"> Это Вы</span>
                                            </div>
                                            <div class="text-sm opacity-50">{{ worker.email }}</div>
                                        </div>
                                    </div>
                                </td>
                                <td>
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

                                <td v-if="worker.isOrganizationAdmin === true && worker.userId === store.state.user.id">
                                    <div class="tooltip" data-tip="Вы не можете удалить себя">
                                        <button class="btn btn-circle btn-outline btn-sm btn-info">
                                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                 stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                                <path stroke-linecap="round" stroke-linejoin="round"
                                                      d="M4.5 12.75l6 6 9-13.5"/>
                                            </svg>
                                        </button>
                                    </div>
                                </td>

                                <td v-else-if="worker.isOrganizationAdmin === true && worker.userId === organization.ownerUserId">
                                    <div class="tooltip" data-tip="Нельзя удалить владельца">
                                        <button class="btn btn-circle btn-outline btn-sm btn-info">
                                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                 stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                                <path stroke-linecap="round" stroke-linejoin="round"
                                                      d="M4.5 12.75l6 6 9-13.5"/>
                                            </svg>
                                        </button>
                                    </div>
                                </td>

                                <td v-else-if="worker.isOrganizationAdmin === true && worker.userId !== store.state.user.id && organization.ownerUserId !== store.state.user.id">
                                    <button class="btn gap-2 btn-sm btn-error btn-disabled"
                                            @click="deleteAdminFromOrganization(worker.id)">
                                        Удалить права
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                             stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                            <path stroke-linecap="round" stroke-linejoin="round"
                                                  d="M6 18L18 6M6 6l12 12"/>
                                        </svg>
                                    </button>
                                </td>

                                <td v-else-if="worker.isOrganizationAdmin === true && worker.userId !== store.state.user.id && organization.ownerUserId === store.state.user.id">
                                    <button class="btn gap-2 btn-outline btn-sm btn-error"
                                            @click="deleteAdminFromOrganization(worker.id)">
                                        Удалить права
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                             stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                            <path stroke-linecap="round" stroke-linejoin="round"
                                                  d="M6 18L18 6M6 6l12 12"/>
                                        </svg>
                                    </button>
                                </td>

                                <td v-else>
                                    <button class="btn gap-2 btn-outline btn-sm btn-success"
                                            @click="addAdminForOrganization(worker.id)">
                                        Добавить права
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                             stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                            <path stroke-linecap="round" stroke-linejoin="round"
                                                  d="M12 4.5v15m7.5-7.5h-15"/>
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
                    <div class="card-actions justify-end mt-5">
                        <a
                                @click="$router.push('/organizations/' + this.$route.params.id + '/workers')"
                                class="btn btn-accent btn-outline">
                            На отдельную страницу
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="flex justify-center m-10" id="org_functions">
            <div class="overflow-x-auto w-1/2 p-5">
                <h2 class="mb-5 mt-5 text-2xl font-bold text-base-color text-center"> Должности организации </h2>
                <table class="table w-full">
                    <!-- head -->
                    <thead>
                    <tr>
                        <th>Название должности</th>
                        <th>Удалить должность</th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr
                            v-for="func in organization.functions"
                            :key="func.id"
                    >
                        <td>
                            <div class="flex items-center space-x-3">
                                <div>
                                    <div class="font-bold">{{ func.name }}</div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <a class="btn gap-2 btn-sm btn-error btn-outline"
                               @click="deleteFunction(func.id)">
                                Удалить должность
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                     stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
                                </svg>
                            </a>
                        </td>
                    </tr>
                    </tbody>
                    <!-- foot -->
                    <tfoot>
                    <tr>
                        <th>Название должности</th>
                        <th>Удалить должность</th>
                    </tr>
                    </tfoot>

                </table>
            </div>
            <div class="overflow-x-auto w-1/2 p-5">
                <div class="flex flex-col items-center">
                    <success-alert
                            :is-success="isSuccessFunction"
                    >Вы успешно создали должность!
                    </success-alert>
                    <success-alert
                            :is-success="isSuccessFunctionDelete"
                    >Вы успешно удалили должность!
                    </success-alert>
                    <error-alert
                            :is-error="isErrorFunction"
                    > {{ errorDescriptionFunction }}
                    </error-alert>
                </div>
                <add-function-form @create="createFunction"/>
            </div>
        </div>

        <div class="flex flex-col items-center">
            <success-alert
                    class="w-1/2"
                    :is-success="isSuccessFinData"
            >Вы успешно заполнили данные!
            </success-alert>
            <error-alert
                    class="w-1/2"
                    :is-error="isErrorFinData"
            > {{ errorDescriptionFinData }}
            </error-alert>
        </div>
        <div class="flex justify-center m-10" id="org_fin_data">
            <div class="card w-1/2 bg-base-200 shadow-xl">
                <div class="card-body">
                    <h2 class="card-title">
                        Финансовые данные организации
                        <a v-if="this.organization.inn !== null" href="#fin-data-info" class="btn btn-primary btn-xs">Посмотреть
                            текущую информацию</a>
                    </h2>
                    <p>
                        Заполните финансовые данные организации, чтобы получать корректные квитанции для сотрудников
                    </p>
                    <fill-fin-organization-data-form @create="saveFinData"/>
                </div>
            </div>
        </div>

        <div class="flex flex-col justify-center m-10" id="org_stats">
            <div>
                <h2 class="mb-5 mt-5 text-2xl font-bold text-base-color text-center">
                    Статистика организации
                </h2>
            </div>
            <div class="stats stats-vertical lg:stats-horizontal shadow bg-base-200">
                <div class="stat">
                    <div class="stat-figure text-secondary">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-8 h-8">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z" />
                        </svg>
                    </div>
                    <div class="stat-title">Всего сотрудников</div>
                    <div class="stat-value">{{ this.stats.countActualWorkers }}</div>
                </div>

                <div class="stat">
                    <div class="stat-figure text-secondary">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-8 h-8">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M15.182 16.318A4.486 4.486 0 0012.016 15a4.486 4.486 0 00-3.198 1.318M21 12a9 9 0 11-18 0 9 9 0 0118 0zM9.75 9.75c0 .414-.168.75-.375.75S9 10.164 9 9.75 9.168 9 9.375 9s.375.336.375.75zm-.375 0h.008v.015h-.008V9.75zm5.625 0c0 .414-.168.75-.375.75s-.375-.336-.375-.75.168-.75.375-.75.375.336.375.75zm-.375 0h.008v.015h-.008V9.75z" />
                        </svg>
                    </div>
                    <div class="stat-title">Уволенные сотрудники</div>
                    <div class="stat-value">{{ this.stats.countFiredWorkers }}</div>
                </div>

                <div class="stat">
                    <div class="stat-figure text-secondary">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-8 h-8">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M9 12h3.75M9 15h3.75M9 18h3.75m3 .75H18a2.25 2.25 0 002.25-2.25V6.108c0-1.135-.845-2.098-1.976-2.192a48.424 48.424 0 00-1.123-.08m-5.801 0c-.065.21-.1.433-.1.664 0 .414.336.75.75.75h4.5a.75.75 0 00.75-.75 2.25 2.25 0 00-.1-.664m-5.8 0A2.251 2.251 0 0113.5 2.25H15c1.012 0 1.867.668 2.15 1.586m-5.8 0c-.376.023-.75.05-1.124.08C9.095 4.01 8.25 4.973 8.25 6.108V8.25m0 0H4.875c-.621 0-1.125.504-1.125 1.125v11.25c0 .621.504 1.125 1.125 1.125h9.75c.621 0 1.125-.504 1.125-1.125V9.375c0-.621-.504-1.125-1.125-1.125H8.25zM6.75 12h.008v.008H6.75V12zm0 3h.008v.008H6.75V15zm0 3h.008v.008H6.75V18z" />
                        </svg>
                    </div>
                    <div class="stat-title">Доски задач</div>
                    <div class="stat-value">{{ this.stats.countTaskBoards }}</div>
                </div>

                <div class="stat">
                    <div class="stat-figure text-secondary">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-8 h-8">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M6 6.878V6a2.25 2.25 0 012.25-2.25h7.5A2.25 2.25 0 0118 6v.878m-12 0c.235-.083.487-.128.75-.128h10.5c.263 0 .515.045.75.128m-12 0A2.25 2.25 0 004.5 9v.878m13.5-3A2.25 2.25 0 0119.5 9v.878m0 0a2.246 2.246 0 00-.75-.128H5.25c-.263 0-.515.045-.75.128m15 0A2.25 2.25 0 0121 12v6a2.25 2.25 0 01-2.25 2.25H5.25A2.25 2.25 0 013 18v-6c0-.98.626-1.813 1.5-2.122" />
                        </svg>
                    </div>
                    <div class="stat-title">Количество задач</div>
                    <div class="stat-value">{{ this.stats.countTasks }}</div>
                </div>


                <div class="stat">
                    <div class="stat-figure text-secondary">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-8 h-8">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 18.75a60.07 60.07 0 0115.797 2.101c.727.198 1.453-.342 1.453-1.096V18.75M3.75 4.5v.75A.75.75 0 013 6h-.75m0 0v-.375c0-.621.504-1.125 1.125-1.125H20.25M2.25 6v9m18-10.5v.75c0 .414.336.75.75.75h.75m-1.5-1.5h.375c.621 0 1.125.504 1.125 1.125v9.75c0 .621-.504 1.125-1.125 1.125h-.375m1.5-1.5H21a.75.75 0 00-.75.75v.75m0 0H3.75m0 0h-.375a1.125 1.125 0 01-1.125-1.125V15m1.5 1.5v-.75A.75.75 0 003 15h-.75M15 10.5a3 3 0 11-6 0 3 3 0 016 0zm3 0h.008v.008H18V10.5zm-12 0h.008v.008H6V10.5z" />
                        </svg>
                    </div>
                    <div class="stat-title">Выплаченная зарплата</div>
                    <div class="stat-value">{{ this.stats.totalSalary }}</div>
                </div>

            </div>
        </div>
    </div>

    <div class="modal" id="fin-data-info">
        <div class="modal-box">
            <h3 class="font-bold text-lg">
                Текущие финансовые данные
            </h3>
            <p class="py-4">
                Ниже представлены текущие финансовые данные компании.
                Если Вы хотите изменить информацию, то просто заполните форму еще раз.
            </p>
            <ul class="menu bg-base-200 p-2 rounded-box">
                <li class="menu-title">
                    <span>Юридическое название</span>
                </li>
                <li><a>{{ this.organization.fullOrganizationTitle }}</a></li>
                <li class="menu-title">
                    <span>ИНН</span>
                </li>
                <li><a>{{ this.organization.inn }}</a></li>
                <li class="menu-title">
                    <span>КПП</span>
                </li>
                <li><a>{{ this.organization.kpp }}</a></li>
                <li class="menu-title">
                    <span>Номер счета</span>
                </li>
                <li><a>{{ this.organization.accountNumber }}</a></li>
                <li class="menu-title">
                    <span>Название банка</span>
                </li>
                <li><a>{{ this.organization.organizationBank }}</a></li>
                <li class="menu-title">
                    <span>БИК банка</span>
                </li>
                <li><a>{{ this.organization.bankBik }}</a></li>
                <li class="menu-title">
                    <span>Банковский счет</span>
                </li>
                <li><a>{{ this.organization.bankAccountNumber }}</a></li>
            </ul>
            <div class="modal-action">
                <a href="#" class="btn">Закрыть</a>
            </div>
        </div>
    </div>
</template>

<script>
import EachOrganizationHeader from "@/components/ui/EachOrganizationHeader.vue";
import AddFunctionForm from "@/components/AddFunctionForm.vue";
import axios from "axios";
import store from "@/store";
import CreateTaskBoardForm from "@/components/CreateTaskBoardForm.vue";
import CreateTagForm from "@/components/CreateTagForm.vue";
import FillFinOrganizationDataForm from "@/components/FillFinOrganizationDataForm.vue";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import Constants from "@/components/constants";

export default {
    data() {
        return {
            isSuccessTaskBoard: false,
            isErrorTaskBoard: false,
            errorDescriptionTaskBoard: '',
            isSuccessFunction: false,
            isErrorFunction: false,
            errorDescriptionFunction: '',
            isSuccessFunctionDelete: false,
            isSuccessTag: false,
            isSuccessTagDelete: false,
            isErrorTag: false,
            errorDescriptionTag: '',
            isSuccessFinData: false,
            isErrorFinData: false,
            errorDescriptionFinData: '',

            stats: {
                countActualWorkers: 0,
                countFiredWorkers: 0,
                countTaskBoards: 0,
                countTasks: 0,
                totalSalary: 0
            }
        }
    },
    computed: {
        store() {
            return store
        }
    },
    components: {
        SuccessAlert, ErrorAlert,
        FillFinOrganizationDataForm,
        CreateTagForm, CreateTaskBoardForm, AddFunctionForm, EachOrganizationHeader
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
    methods: {
        async createFunction(func) {
            if (func.name === '') {
                this.isErrorFunction = true;
                this.errorDescriptionFunction = Constants.ERROR_EMPTY_FIELDS
                return
            }
            await axios.post("http://localhost:8080/api/tracker/v1/function/create", {
                name: func.name,
                organizationId: this.$route.params.id
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload);

                this.isSuccessFunction = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                this.isErrorFunction = true;
                this.errorDescriptionFunction = Constants.ERROR_SERVER_DESCRIPTION
                console.log(response);
            })
        },
        async createBoard(board) {
            if (board.name === '') {
                this.isErrorTaskBoard = true;
                this.errorDescriptionTaskBoard = Constants.ERROR_EMPTY_FIELDS
                return
            }
            await axios.post("http://localhost:8080/api/tracker/v1/taskBoard/create", {
                name: board.name,
                organizationId: this.$route.params.id
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)

                this.isSuccessTaskBoard = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                this.isErrorTaskBoard = true;
                this.errorDescriptionTaskBoard = Constants.ERROR_SERVER_DESCRIPTION;
                console.log(response)
            })
        },
        async createTag(tag) {
            if (tag.name === '' || tag.description === '') {
                this.isErrorTag = true;
                this.errorDescriptionTag = Constants.ERROR_EMPTY_FIELDS;
                return
            }
            await axios.post("http://localhost:8080/api/tracker/v1/tag/create", {
                name: tag.name,
                organizationId: this.organization.id
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)

                this.isSuccessTag = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                this.isErrorTag = true;
                this.errorDescriptionTag = Constants.ERROR_SERVER_DESCRIPTION;
                console.log(response.data.payload)
            })
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
        async saveFinData(finData) {
            if (finData.fullOrganizationTitle === '' || finData.inn === '' || finData.kpp === ''
                || finData.accountNumber === '' || finData.organizationBank === ''
                || finData.bankBik === '' || finData.bankAccountNumber === '') {
                this.isErrorFinData = true;
                this.errorDescriptionFinData = Constants.ERROR_EMPTY_FIELDS;
                return
            }
            await axios.put("http://localhost:8080/api/tracker/v1/organization/fill-bank-info/" + this.organization.id, {
                fullOrganizationTitle: finData.fullOrganizationTitle,
                inn: finData.inn,
                kpp: finData.kpp,
                accountNumber: finData.accountNumber,
                organizationBank: finData.organizationBank,
                bankBik: finData.bankBik,
                bankAccountNumber: finData.bankAccountNumber
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)

                this.isSuccessFinData = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                this.isErrorFinData = true;
                this.errorDescriptionFinData = Constants.ERROR_SERVER_DESCRIPTION;
                console.log(response)
            })
        },
        async deleteFunction(functionId) {
            await axios.delete("http://localhost:8080/api/tracker/v1/function/delete/" + functionId, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.isSuccessFunctionDelete = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                console.log(response);
                this.isErrorFunction = true;
                this.errorDescriptionFunction = Constants.ERROR_SERVER_DESCRIPTION;
            })
        },
        async deleteTag(tagId) {
            await axios.delete("http://localhost:8080/api/tracker/v1/tag/delete/" + tagId, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload);
                this.isSuccessTagDelete = true;
                setTimeout(() => this.$router.go(this.$router.currentRoute), 1000)
            }).catch((response) => {
                console.log(response);
                this.isErrorTag = true;
                this.errorDescriptionTag = Constants.ERROR_SERVER_DESCRIPTION;
            })
        },
        async getOrganizationStats() {
            await axios.get("http://localhost:8080/api/tracker/v1/organization/stats/" + this.$route.params.id, {
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
        this.getOrganizationStats();
    }
}
</script>