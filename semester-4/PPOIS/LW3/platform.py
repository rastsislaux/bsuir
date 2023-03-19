import pygame


class Platform:

    def __init__(self, x, y, image, type_id):
        self.image = image
        self.rect = pygame.Rect(x, y, 32, 32)

        self.type_id = type_id
        self.type = "Platform"

        self.shaking = False
        self.shaking_up = False
        self.shake_offset = 0

        if self.type_id == 22:
            self.current_image = 0
            self.image_tick = 0
            self.is_activated = False
            self.bonus = "coin"

    def update(self):
        if self.type_id == 22:
            self.image_tick += 1
            if self.image_tick == 50:
                self.current_image = 1
            elif self.image_tick == 60:
                self.current_image = 2
            elif self.image_tick == 70:
                self.current_image = 1
            elif self.image_tick == 80:
                self.current_image = 0
                self.image_tick = 0

    def shake(self):
        if self.shaking_up:
            self.shake_offset -= 2
            self.rect.y -= 2
        else:
            self.shake_offset += 2
            self.rect.y += 2

        if self.shake_offset == -20:
            self.shaking_up = False
        if self.shake_offset == 0:
            self.shaking = False
            self.shaking_up = True

    def spawn_bonus(self, game):
        self.is_activated = True
        self.shaking = True
        self.image_tick = 0
        self.current_image = 3

        if self.bonus == "mushroom":
            game.sound_manager.play("mushroom_appear", 0, 0.5)
            if game.world.player.power_level == 0:
                game.world.spawn_mushroom(self.rect.x, self.rect.y)
            else:
                game.world.spawn_flower(self.rect.x, self.rect.y)

        elif self.bonus == "coin":
            game.sound_manager.play("coin", 0, 0.5)
            game.world.spawn_debris(self.rect.x + 8, self.rect.y - 32, 1)
            game.world.player.add_coins(1)
            game.world.player.add_score(200)

    def destroy(self, game):
        game.world.spawn_debris(self.rect.x, self.rect.y, 0)
        game.world.remove_object(self)

    def render(self, game):

        if self.type_id == 22:
            if not self.is_activated:
                self.update()
            elif self.shaking:
                self.shake()
            game.screen.blit(self.image[self.current_image], game.world.camera.apply(self))

        elif self.type_id == 23 and self.shaking:
            self.shake()
            game.screen.blit(self.image, game.world.camera.apply(self))

        else:
            game.screen.blit(self.image, game.world.camera.apply(self))
