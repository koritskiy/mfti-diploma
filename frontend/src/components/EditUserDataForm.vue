<template>
    <form @submit.prevent method="post">
        <text-input
                label="Ваше имя"
                placeholder="Например, Александр"
                v-model:model-value="user.firstName"
                class="mt-3"
        />

        <text-input
                label="Ваша фамилия"
                placeholder="Например, Иванов"
                v-model:model-value="user.lastName"
                class="mt-3"
        />

        <text-input
                label="Ваш новый пароль"
                placeholder="Введите пароль"
                input-type="password"
                v-model:model-value="user.password"
                class="mt-3"
        />

        <text-input
                label="Подтвердите Ваш новый пароль"
                placeholder="Введите пароль еще раз"
                input-type="password"
                class="mt-3"
        />

        <div class="flex justify-center">
            <success-outline-button @click="editUserData" class="">Изменить данные</success-outline-button>
        </div>
    </form>
</template>

<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";
import TextInput from "@/components/ui/TextInput.vue";
import store from "@/store";

export default {
    components: {TextInput, SuccessOutlineButton},
    data() {
        return {
            user: {
                firstName: store.state.user.firstName,
                lastName: store.state.user.lastName,
                password: '',
                passwordConfirm: ''
            }
        }
    },
    methods: {
        editUserData() {
            this.$emit('create', this.user)
        }
    }
}
</script>