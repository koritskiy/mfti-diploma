<template>
    <div class="flex justify-center mt-10">
        <div class="tabs tabs-boxed">
            <a
                    @click="this.$router.push('/account/settings')"
                    class="tab tab-active">
                Редактирование данных
            </a>
            <a
                    @click="this.$router.push('/account/settings/approve/email')"
                    class="tab">
                Подтверждение почты
            </a>
            <a
                    @click="this.$router.push('/account/settings/upload/token')"
                    class="tab">
                Загрузка токена
            </a>
        </div>
    </div>

    <div class="flex flex-col items-center">
        <success-alert
                :is-success="isSuccess"
                class="w-1/2"
        >Вы успешно изменили данные!
        </success-alert>
        <error-alert
                :is-error="isError"
                class="w-1/2"
        > {{ errorDescription }}
        </error-alert>
    </div>

    <div class="flex justify-center mt-10 mb-10">
        <div class="card bg-base-200 shadow-xl">
            <div class="card-body">
                <h2 class="card-title">Изменение данных пользователя</h2>
                <p>
                    Измените данные, исправив их в соответствующих полях
                </p>
                <edit-user-data-form @create="editUserData"/>
            </div>
        </div>
    </div>
</template>

<script>
import EditUserDataForm from "@/components/EditUserDataForm.vue";
import axios from "axios";
import store from "@/store";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";
import Constants from "@/components/constants";

export default {
    components: {SuccessAlert, ErrorAlert, EditUserDataForm},
    mounted() {
        this.$store.commit('setPageName', 'account-settings');
    },
    data() {
        return {
            isSuccess: false,
            isError: false,
            errorDescription: ''
        }
    },
    methods: {
        async editUserData(user) {
            console.log(user)
            await axios.put("http://localhost:8080/api/auth/v1/user/edit", {
                userId: store.state.user.id,
                firstName: user.firstName,
                lastName: user.lastName,
                password: user.password
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload)
                const user = {
                    id: response.data.payload.id,
                    firstName: response.data.payload.firstName,
                    lastName: response.data.payload.lastName,
                    isUserAdmin: response.data.payload.isUserAdmin,
                    gitHubToken: response.data.payload.gitHubToken
                }
                this.$store.commit('setUser', user);

                this.isSuccess = true;
            }).catch((response) => {
                console.log(response)
                this.isError = true;
                this.errorDescription = Constants.ERROR_SERVER_DESCRIPTION;
            })
        }
    }
}
</script>