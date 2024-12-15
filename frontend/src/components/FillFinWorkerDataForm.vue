<template>
    <form @submit.prevent method="post">
        <text-input
                label="Ваше отчество"
                placeholder="Например, Юрьевич"
                v-model:model-value="finData.middleName"
        />

        <div class="flex">
            <text-input
                    label="Ваш ИНН"
                    placeholder="Например, 12345678"
                    v-model:model-value="finData.inn"
                    class="mt-3 mr-3"
            />

            <text-input
                    label="Название Вашего банка"
                    placeholder="Например, Сбербанк"
                    v-model:model-value="finData.workerBank"
                    class="mt-3"
            />
        </div>

        <text-input
                label="БИК Вашего банка"
                placeholder="Например, 1234"
                v-model:model-value="finData.bankBik"
                class="mt-3"
        />

        <text-input
                label="Банковский счет"
                placeholder="Например, 12345678"
                v-model:model-value="finData.bankAccountNumber"
                class="mt-3"
        />

        <text-input
                label="Ваш номер счета"
                placeholder="Например, 12345678"
                v-model:model-value="finData.accountNumber"
                class="mt-3"
        />

        <div class="flex justify-center">
            <success-outline-button @click="fillFinData" class="">Сохранить информацию</success-outline-button>
        </div>
    </form>
</template>
<script>
import TextInput from "@/components/ui/TextInput.vue";
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";

export default {
    components: {SuccessOutlineButton, TextInput},
    data() {
        return {
            finData: {
                middleName: '',
                inn: '',
                workerBank: '',
                bankBik: '',
                bankAccountNumber: '',
                accountNumber: ''
            }
        }
    },
    methods: {
        fillFinData() {
            this.$emit('create', this.finData)
        }
    }
}
</script>