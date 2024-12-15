<template>
    <div class="flex justify-center m-10 lg:ml-0 lg:mr-0">
        <div class="card lg:w-2/3 bg-base-200 shadow-xl">
            <div class="lg:flex">
                <div class="card-body items-center text-center">
                    <h1 class="card-title font-bold">{{ user.firstName }} {{ user.lastName }}, добрый день!</h1>
                    <p>
                        Добро пожаловать в Ваш аккаунт! Пока что он пустоват, однако в Ваших силах это исправить!
                        Нажимайте на кнопки ниже, чтобы начать управлять своим бизнесом!
                    </p>

                    <ul class="steps steps-vertical lg:steps-horizontal">
                        <li class="step step-success">Создать аккаунт</li>
                        <li class="step">
                            <a @click="this.$router.push('/create-organization')"
                               class="hover:text-primary hover:underline hover:cursor-pointer transition duration-200">
                                Рассказать о Вашей компании
                            </a>
                        </li>
                        <li class="step">Пригласить сотрудников</li>
                        <li class="step">Создать доску задач</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <div class="lg:flex lg:flex-wrap lg:justify-center">
        <organization-card
                v-for="organization in organizations"
                :organization="organization"
                :key="organization.id"
        />
    </div>

    <div class="flex flex-col items-center">
        <success-alert
                class="w-1/2"
                :is-success="isSuccess"
        >Вы успешно заполнили данные!
        </success-alert>
        <error-alert
                class="w-1/2"
                :is-error="isError"
        > {{ errorDescription }}
        </error-alert>
    </div>

    <div class="lg:flex lg:flex-wrap lg:justify-center">
        <div class="card bg-base-200 shadow-xl m-10 mt-0">
            <div class="card-body">
                <h2 class="card-title">
                    Финансовые данные сотрудника
                    <a v-if="this.worker.inn !== null" href="#fin-data-info" class="btn btn-primary btn-xs">Посмотреть
                        текущую информацию</a>
                </h2>
                <p>
                    Заполните свои финансовые данные организации, чтобы работодателю было удобно выплачивать зарплату
                </p>
                <fill-fin-worker-data-form @create="fillFinData"/>
            </div>
        </div>
    </div>

    <div class="modal" id="fin-data-info">
        <div class="modal-box">
            <h3 class="font-bold text-lg">
                Текущие финансовые данные
            </h3>
            <p class="py-4">
                Ниже представлены Ваши текущие финансовые данные.
                Если Вы хотите изменить информацию, то просто заполните форму еще раз.
            </p>
            <ul class="menu bg-base-200 p-2 rounded-box">
                <li class="menu-title">
                    <span>Ваш ИНН</span>
                </li>
                <li><a>{{ this.worker.inn }}</a></li>
                <li class="menu-title">
                    <span>Ваш банк</span>
                </li>
                <li><a>{{ this.worker.workerBank }}</a></li>
                <li class="menu-title">
                    <span>БИК банка</span>
                </li>
                <li><a>{{ this.worker.bankBik }}</a></li>
                <li class="menu-title">
                    <span>Банковский счет</span>
                </li>
                <li><a>{{ this.worker.bankAccountNumber }}</a></li>
                <li class="menu-title">
                    <span>Ваш счет</span>
                </li>
                <li><a>{{ this.worker.accountNumber }}</a></li>
            </ul>
            <div class="modal-action">
                <a href="#" class="btn">Закрыть</a>
            </div>
        </div>
    </div>
</template>

<script>
import {mapState} from "vuex";
import OrganizationCard from "@/components/OrganizationCard.vue";
import axios from "axios";
import store from "@/store";
import FillFinWorkerDataForm from "@/components/FillFinWorkerDataForm.vue";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import Constants from "@/components/constants";
import defaultAvatar from "/src/static/default-avatar-2.png"

export default {
    components: {SuccessAlert, ErrorAlert, FillFinWorkerDataForm, OrganizationCard},
    data() {
        return {
            organizations: [],
            worker: '',
            isSuccess: false,
            isError: false,
            errorDescription: '',

            defaultAvatar: defaultAvatar
        }
    },
    computed: {
        ...mapState({
            user: state => state.user,
            token: state => state.token
        })
    },
    methods: {
        async getOrganizations() {
            await axios.get(
                'http://localhost:8080/api/tracker/v1/organization/my',
                {
                    headers: {
                        Authorization: this.token
                    }
                }
            ).then((response) => {
                this.organizations = response.data.payload.organizations;
                console.log(response);
            }).catch((error) => {
                console.log(error);
            })
        },
        async getWorkerByUserId() {
            await axios.get("http://localhost:8080/api/tracker/v1/worker/me", {
                headers: {
                    Authorization: this.token
                }
            }).then((response) => {
                this.worker = response.data.payload;
                console.log(response.data.payload)
            }).catch((response) => {
                console.log(response)
            })
        },
        async fillFinData(finData) {
            await axios.post("http://localhost:8080/api/tracker/v1/worker/fill-pay-info/" + this.worker.id, {
                middleName: finData.middleName,
                inn: finData.inn,
                workerBank: finData.workerBank,
                bankBik: finData.bankBik,
                bankAccountNumber: finData.bankAccountNumber,
                accountNumber: finData.accountNumber
            }, {
                headers: {
                    Authorization: this.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                this.worker = response.data.payload;
                this.isSuccess = true;
            }).catch(() => {
                this.isError = true;
                this.errorDescription = Constants.ERROR_SERVER_DESCRIPTION;
            })
        },
        printToken() {
            console.log(store.state.token)
            console.log(store.state.user)
        }
    },
    mounted() {
        this.$store.commit('setPageName', 'account');
        this.getOrganizations();
        this.printToken();
        this.getWorkerByUserId();
    }
}
</script>

<style lang="scss" scoped>

</style>