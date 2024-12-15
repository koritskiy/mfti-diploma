<template>
    <form @submit.prevent method="post">
        <div class="lg:flex lg:flex-row">
            <text-input
                label="Фамилия кандидата"
                placeholder="Например, Иванов"
                class="mt-3 mr-3"
                v-model:model-value="candidate.lastName"
            />

            <text-input
                label="Имя кандидата"
                placeholder="Например, Александр"
                class="mt-3 mr-3"
                v-model:model-value="candidate.firstName"
            />

            <text-input
                label="Отчество кандидата"
                placeholder="Например, Иванович"
                class="mt-3"
                v-model:model-value="candidate.middleName"
            />
        </div>

        <text-input
            label="Почта кандидата"
            input-type="email"
            placeholder="Например, abc@gmail.com"
            class="mt-3"
            v-model:model-value="candidate.email"
        />

        <text-input
            label="Ссылка на резюме кандидата"
            placeholder="Например, ссылка на HeadHunter"
            class="mt-3"
            v-model:model-value="candidate.resumeLink"
        />

        <div class="lg:flex lg:flex-row">
            <text-input
                label="Желаемая должность"
                placeholder="Например, программист"
                class="mt-3 mr-3"
                v-model:model-value="candidate.position"
            />

            <text-input
                label="Желаемый оклад"
                placeholder="Например, 100 тысяч"
                class="mt-3"
                v-model:model-value="candidate.tariff"
            />
        </div>

        <div class="lg:flex lg:flex-row">
            <text-input
                label="Телефон кандидата"
                placeholder="Например, +79031231212"
                class="mt-3 mr-3"
                v-model:model-value="candidate.phone"
            />

            <text-input
                label="Ник кандидата в телеграме"
                placeholder="Например, @korizza"
                class="mt-3"
                v-model:model-value="candidate.telegramName"
            />
        </div>

        <div class="flex justify-center">
            <success-outline-button @click="createCandidate" class="">Добавить кандидата</success-outline-button>
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
            candidate: {
                lastName: '',
                firstName: '',
                middleName: '',
                email: '',
                resumeLink: '',
                position: '',
                tariff: '',
                phone: '',
                telegramName: ''
            }
        }
    },
    methods: {
        createCandidate() {
            this.$emit('create', this.candidate)
        }
    }
}
</script>