<template>
    <div class="flex justify-center m-10 lg:ml-0 lg:mr-0">
        <div class="card bg-base-200 shadow-xl basis-1/2">
            <div class="card-body">
                <div class="flex flex-col items-center">
                    <success-alert
                        :is-success="isSuccess"
                    >Пароль успешно изменен!
                    </success-alert>
                    <error-alert
                        class="w-1/2"
                        :is-error="isError"
                    > {{ errorDescription }}
                    </error-alert>
                </div>
                <h2 class="card-title">
                    Восстановление пароля
                </h2>
                <p>
                    Введите Вашу почту и после этого Вам придет новый пароль.
                    Важно, что пароль придет, только если почта подтверждена. Если почта не подтверждена,
                    то напишите разработчикам.
                </p>
                <form @submit.prevent method="post">
                    <text-input
                        label="Ваша почта"
                        placeholder="Например, example@gmail.com"
                        class="mt-3"
                        v-model:model-value="email"
                    />

                    <div class="flex justify-center">
                        <success-outline-button @click="forgotPassword" class="">Получить пароль</success-outline-button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";
import TextInput from "@/components/ui/TextInput.vue";
import axios from "axios";
import store from "@/store";
import Constants from "@/components/constants";
import ErrorAlert from "@/components/ui/ErrorAlert.vue";
import SuccessAlert from "@/components/ui/SuccessAlert.vue";

export default {
    components: {SuccessAlert, ErrorAlert, TextInput, SuccessOutlineButton},
    data() {
        return {
            email: '',
            isSuccess: false,
            isError: false,
            errorDescription: ''
        }
    },
    methods: {
        async forgotPassword() {
            await axios.put("http://localhost:8080/api/auth/v1/forgot/password", {
                email: this.email
            }, {
                headers: {
                    Authorization: store.state.token
                }
            }).then((response) => {
                console.log(response.data.payload);
                this.isSuccess = true;
            }).catch((response) => {
                console.log(response);
                this.isError = true;
                this.errorDescription = Constants.ERROR_SERVER_DESCRIPTION;
            })
        }
    }
}
</script>