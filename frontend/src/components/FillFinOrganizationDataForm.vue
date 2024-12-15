<template>
    <form @submit.prevent method="post">
        <text-input
                label="Полное юридическое название компании"
                placeholder="Например, ООО Ромашка"
                v-model:model-value="finData.fullOrganizationTitle"
        />

        <div class="flex">
            <text-input
                    label="ИНН компании"
                    placeholder="Например, 12345678"
                    v-model:model-value="finData.inn"
                    class="mt-3 mr-3"
            />

            <text-input
                    label="КПП банка"
                    placeholder="Например, 1234"
                    v-model:model-value="finData.kpp"
                    class="mt-3"
            />
        </div>

        <text-input
                label="Номер счета в банке"
                placeholder="Например, 12345678"
                v-model:model-value="finData.accountNumber"
                class="mt-3"
        />

        <div class="flex">
            <text-input
                    label="Название банка"
                    placeholder="Например, Сбербанк"
                    v-model:model-value="finData.organizationBank"
                    class="mt-3 mr-3"
            />

            <text-input
                    label="БИК банка компании"
                    placeholder="Например, 12345678"
                    v-model:model-value="finData.bankBik"
                    class="mt-3"
            />
        </div>

        <text-input
                label="Банковский счет"
                placeholder="Например, 12345678"
                v-model:model-value="finData.bankAccountNumber"
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
                fullOrganizationTitle: '',
                inn: '',
                kpp: '',
                accountNumber: '',
                organizationBank: '',
                bankBik: '',
                bankAccountNumber: ''
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