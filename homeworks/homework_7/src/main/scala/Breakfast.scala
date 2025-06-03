   * Функция должна эмулировать приготовление завтрака. Продолжительные операции необходимо эмулировать через ZIO.sleep.
   * Правила приготовления следующие:
   *  1. Нобходимо вскипятить воду (время кипячения waterBoilingTime)
   *  2. Параллельно с этим нужно жарить яичницу eggsFiringTime
   *  3. Параллельно с этим готовим салат:
   *    * сначала режим  огурцы
   *    * после этого режим помидоры
   *    * после этого добавляем в салат сметану
   *  4. После того, как закипит вода необходимо заварить чай, время заваривания чая teaBrewingTime
   *  5. После того, как всё готово, можно завтракать
   *     2. Параллельно с этим нужно жарить яичницу eggsFiringTime
   *     3. Параллельно с этим готовим салат:
   *     * сначала режим  огурцы
   *     * после этого режим помидоры
   *     * после этого добавляем в салат сметану
   *     4. После того, как закипит вода необходимо заварить чай, время заваривания чая teaBrewingTime
   *     5. После того, как всё готово, можно завтракать
   *
   * @param eggsFiringTime время жарки яичницы
   * @param eggsFiringTime   время жарки яичницы
   * @param waterBoilingTime время кипячения воды
   * @param saladInfoTime информация о времени для приготовления салата
   * @param teaBrewingTime время заваривания чая
   * @param saladInfoTime    информация о времени для приготовления салата
   * @param teaBrewingTime   время заваривания чая
   * @return Мапу с информацией о том, когда завершился очередной этап (eggs, water, saladWithSourCream, tea)
   */
  def makeBreakfast(eggsFiringTime: Duration,
                    waterBoilingTime: Duration,
                    saladInfoTime: SaladInfoTime,
                    teaBrewingTime: Duration): ZIO[Any, Throwable, Map[String, LocalDateTime]] = ???

                    teaBrewingTime: Duration): ZIO[Any, Throwable, Map[String, LocalDateTime]] = for {
    eggs <- ZIO.sleep(eggsFiringTime).as(LocalDateTime.now()).fork
    water <- ZIO.sleep(waterBoilingTime).as(LocalDateTime.now()).fork
    salad <- ZIO.sleep(saladInfoTime.tomatoTime.plus(saladInfoTime.cucumberTime)).as(LocalDateTime.now()).fork
    tea <- ZIO.sleep(teaBrewingTime).as(LocalDateTime.now()).fork

    eggsTime <- eggs.join
    waterTime <- water.join
    saladTime <- salad.join
    teaTime <- tea.join
  } yield Map(
    "eggs" -> eggsTime,
    "water" -> waterTime,
    "saladWithSourCream" -> saladTime,
    "tea" -> teaTime
  )


  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ZIO.succeed(println("Done"))
