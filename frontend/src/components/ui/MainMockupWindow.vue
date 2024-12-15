<template>
    <div class="w-full mockup-window border bg-base-300">
        <div class="px-4 py-8 bg-base-100">
            <div class="chat chat-start">
                <div class="chat-image avatar">
                    <div class="w-10 rounded-full ring ring-offset-base-200 ring-offset-2">
                        <img :src="manImage" alt="Иконка сотрудника" />
                    </div>
                </div>
                <div class="chat-bubble">
                    {{ firstText }}
                </div>
            </div>

            <div class="chat chat-end">
                <div class="chat-image avatar">
                    <div class="w-10 rounded-full ring ring-offset-base-200 ring-offset-2">
                        <img :src="ownerImage" alt="Иконка руководителя" />
                    </div>
                </div>
                <div class="chat-bubble">
                    {{ secondText }}
                </div>
            </div>

            <div class="chat chat-start">
                <div class="chat-image avatar">
                    <div class="w-10 rounded-full ring ring-offset-base-200 ring-offset-2">
                        <img :src="manImage" alt="Иконка сотрудника" />
                    </div>
                </div>
                <div class="chat-bubble">
                    {{ thirdText }}
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import manImage from "/src/static/man.png"
import ownerImage from "/src/static/owner.png"

export default {
    // todo добавить диалоги в отдельные компоненты
    name: 'MainMockupWindow',
    data() {
        return {
            firstText: 'Я сделал задачу в срок, шеф!',
            secondText: 'Это круто! Отметил это в teamsync?',
            thirdText: 'Конечно, он такой крутой!',

            manImage: manImage,
            ownerImage: ownerImage
        }
    }
}
</script>

<style lang="scss" scoped>

</style>